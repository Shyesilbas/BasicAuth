package org.example.basicauth.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.basicauth.Model.Role;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private Long personalId;

    @Column(unique = true)
    private String username;

    private String password;
    private String email;
    private String profession;
    private BigDecimal salary;
}