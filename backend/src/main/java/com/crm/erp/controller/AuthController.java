package com.crm.erp.controller;

import com.crm.erp.dto.AuthResponse;
import com.crm.erp.dto.LoginRequest;
import com.crm.erp.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles user login.
     *
     * @param loginRequest DTO containing email & password.
     * @return ResponseEntity with JWT token or error message.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for email: {}", loginRequest.getEmail());

        // Delegate authentication to service
        ResponseEntity<?> response = authService.authenticate(loginRequest);

        logger.info("Login response: {}", response.getStatusCode());
        return response;
    }
}
