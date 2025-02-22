package com.crm.erp.dto;

import com.crm.erp.model.InvoiceStatus;
import java.time.LocalDate;

public class InvoiceDTO {
    private Long id;
    private String invoiceNumber;
    private Double amount;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private InvoiceStatus status;
    private Long clientId;

    public InvoiceDTO(Long id, String invoiceNumber, Double amount, LocalDate issueDate,
                      LocalDate dueDate, InvoiceStatus status, Long clientId) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
        this.clientId = clientId;
    }

    public Long getId() { return id; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public Double getAmount() { return amount; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public InvoiceStatus getStatus() { return status; }
    public Long getClientId() { return clientId; }
}
