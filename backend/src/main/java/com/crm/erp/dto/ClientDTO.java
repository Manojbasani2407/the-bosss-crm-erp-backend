package com.crm.erp.dto;

import lombok.Data;

/**
 * DTO for Client responses.
 */
@Data
public class ClientDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
}
