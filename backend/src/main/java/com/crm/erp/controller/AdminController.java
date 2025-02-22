package com.crm.erp.controller;

import com.crm.erp.model.User;
import com.crm.erp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/approve/{userId}")
    public ResponseEntity<String> approveUser(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.activate(); // ✅ Use method instead of direct field access
                    userRepository.save(user);
                    return ResponseEntity.ok("✅ User access granted.");
                })
                .orElseGet(() -> ResponseEntity.status(404).body("❌ User not found."));
    }

    @PutMapping("/assign-role/{userId}")
    public ResponseEntity<String> assignRole(@PathVariable Long userId, @RequestParam String role) {
        return userRepository.findById(userId)
                .map(user -> {
                    if (!isValidRole(role)) {
                        return ResponseEntity.badRequest().body("❌ Invalid role: " + role);
                    }
                    user.assignRole(role.toUpperCase()); // ✅ Use method instead of direct field access
                    userRepository.save(user);
                    return ResponseEntity.ok("✅ User role updated to " + role.toUpperCase());
                })
                .orElseGet(() -> ResponseEntity.status(404).body("❌ User not found."));
    }

    // ✅ Role validation helper method
    private boolean isValidRole(String role) {
        return role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("USER") || role.equalsIgnoreCase("MANAGER");
    }
}
