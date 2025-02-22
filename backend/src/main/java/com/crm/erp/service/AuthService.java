package com.crm.erp.service;

import com.crm.erp.dto.AuthResponse;
import com.crm.erp.dto.LoginRequest;
import com.crm.erp.model.User;
import com.crm.erp.repository.UserRepository;
import com.crm.erp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user by email and password.
     *
     * @param loginRequest DTO containing user credentials.
     * @return ResponseEntity with JWT token if successful, error otherwise.
     */
    public ResponseEntity<?> authenticate(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Fetch user by email
        User user = userRepository.findByEmail(email)
                .orElse(null);

        // If user does not exist or password does not match, return error
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(email);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
