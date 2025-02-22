package com.crm.erp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum InvoiceStatus {
    PAID, PENDING, OVERDUE;

    @JsonCreator
    public static InvoiceStatus fromString(String value) {
        for (InvoiceStatus status : InvoiceStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {  // Case-insensitive match
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid InvoiceStatus: " + value);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
