package com.crm.erp.service;

import com.crm.erp.model.User;
import com.crm.erp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j // Enables logging
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Loads user details by email for authentication.
     * Ensures the password is hashed and matches stored credentials.
     *
     * @param email User's email for authentication.
     * @return UserDetails object with user credentials and roles.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("❌ User not found: " + email));

        log.info("🔍 Found user: {} | Hashed Password: {}", user.getEmail(), user.getPassword());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword()) // Password is already hashed
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())))
                .build();
    }

    /**
     * Saves a new user with a securely encrypted password.
     * Ensures that the password is hashed before storage.
     *
     * @param user The user entity to be saved.
     * @return The saved user with an encrypted password.
     */
    public User saveUser(User user) {
        log.info("🔑 Encrypting password for user: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
        return userRepository.save(user);
    }

    /**
     * Validates user login credentials (email & password).
     *
     * @param email    User's email.
     * @param rawPassword Plain text password entered by the user.
     * @return UserDetails if authentication is successful.
     * @throws UsernameNotFoundException if user not found or password does not match.
     */
    public UserDetails authenticateUser(String email, String rawPassword) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("❌ User not found: " + email));

        log.info("🔑 Checking password for user: {}", email);

        // Ensure the password matches
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            log.warn("❌ Invalid password for user: {}", email);
            throw new UsernameNotFoundException("Invalid credentials");
        }

        log.info("✅ Authentication successful for user: {}", email);
        return loadUserByUsername(email);
    }

    /**
     * Finds a user by email.
     *
     * @param email The email to search for.
     * @return An Optional containing the user if found.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
