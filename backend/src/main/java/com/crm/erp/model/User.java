package com.crm.erp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isActive = false;  // ✅ Default value for new users

    @Column(nullable = false)
    private String role = "USER";  // ✅ Default role

    public void activate() {
        this.isActive = true;
    }

    public void assignRole(String role) {
        this.role = role;
    }
}
