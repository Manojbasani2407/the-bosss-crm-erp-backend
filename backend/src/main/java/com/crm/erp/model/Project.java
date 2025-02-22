package com.crm.erp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entity representing a Project.
 */
@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 50)
    private String status = "New Onboarding"; // ✅ Default status

    @Column(nullable = false)
    private Double amountSpent = 0.0;

    @Column(nullable = false)
    private Double budget = 0.0;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedDeliveryDate;

    @Column(nullable = false, length = 255)
    private String productOwner = "Unknown";

    @Column(columnDefinition = "TEXT")
    private String lastUpdateComments;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    @Column(nullable = false, updatable = false) // ✅ Cannot be updated after creation
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate onboardDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnoreProperties("projects") // ✅ Helps fix serialization issues
    private Client client;

    /**
     * Auto-set onboardDate if not provided before persisting.
     */
    @PrePersist
    protected void onCreate() {
        if (this.onboardDate == null) {
            this.onboardDate = LocalDate.now();
        }
    }
}
