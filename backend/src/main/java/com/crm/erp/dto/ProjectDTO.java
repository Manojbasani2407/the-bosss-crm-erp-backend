package com.crm.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private Long projectId; // NEW field for project ID
    private String name;
    private Long clientId;
    private String productOwner;
    private LocalDate expectedDeliveryDate;
    private LocalDate deadline;
    private Double budget;
    private Double amountSpent;
    private String status;
    private String lastUpdateComments;
    private LocalDate onboardDate;
}
