package org.example.basicauth.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCustomer {
    private Long personalId;
    private String username;
    private String password;
    private String email;
    private String profession;
    private BigDecimal salary;
}
