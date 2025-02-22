package com.crm.erp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entity representing a deleted project (archived).
 */
@Entity
@Table(name = "deleted_projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeletedProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Double amountSpent = 0.0;

    @Column(nullable = false)
    private Double budget = 0.0;

    @Column(nullable = false, length = 255)
    private String productOwner;

    @Column(columnDefinition = "TEXT")
    private String lastUpdateComments;

    @Column(nullable = false)
    private LocalDate expectedDeliveryDate;  // ✅ Changed from String to LocalDate

    @Column(nullable = false)
    private LocalDate deadline;  // ✅ Changed from String to LocalDate

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /**
     * Constructor to convert an existing Project into a DeletedProject.
     */
    public DeletedProject(Project project) {
        this.name = project.getName();
        this.status = project.getStatus();
        this.amountSpent = project.getAmountSpent();
        this.budget = project.getBudget();
        this.productOwner = project.getProductOwner();
        this.lastUpdateComments = project.getLastUpdateComments();
        this.expectedDeliveryDate = project.getExpectedDeliveryDate();  // ✅ No need to convert to String
        this.deadline = project.getDeadline();  // ✅ No need to convert to String
        this.client = project.getClient();
    }
}
