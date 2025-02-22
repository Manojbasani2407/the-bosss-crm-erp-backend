package com.crm.erp.dto;

import com.crm.erp.model.InvoiceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO representing Invoice details.
 */
@Data
@NoArgsConstructor  // Default constructor for Jackson
@AllArgsConstructor // Parameterized constructor for easy object creation
public class InvoiceDTO {

    private Long id;
    private String invoiceNumber;
    private Double amount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private InvoiceStatus status;
    private Long clientId;
}
