package com.crm.erp.dto;

import java.time.LocalDate;

public class ProjectDTO {
    private String name;
    private Long clientId;
    private String productOwner;
    private LocalDate expectedDeliveryDate;
    private LocalDate deadline; // New field for deadline
    private Double budget;
    private Double amountSpent;
    private String status;
    private String lastUpdateComments;
    private LocalDate onboardDate;

    // Getters and Setters

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getClientId() {
        return clientId;
    }
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    public String getProductOwner() {
        return productOwner;
    }
    public void setProductOwner(String productOwner) {
        this.productOwner = productOwner;
    }
    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }
    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }
    public LocalDate getDeadline() {
        return deadline;
    }
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
    public Double getBudget() {
        return budget;
    }
    public void setBudget(Double budget) {
        this.budget = budget;
    }
    public Double getAmountSpent() {
        return amountSpent;
    }
    public void setAmountSpent(Double amountSpent) {
        this.amountSpent = amountSpent;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getLastUpdateComments() {
        return lastUpdateComments;
    }
    public void setLastUpdateComments(String lastUpdateComments) {
        this.lastUpdateComments = lastUpdateComments;
    }
    public LocalDate getOnboardDate() {
        return onboardDate;
    }
    public void setOnboardDate(LocalDate onboardDate) {
        this.onboardDate = onboardDate;
    }
}
