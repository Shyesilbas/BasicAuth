package org.example.basicauth.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private Long personalId;
    private String username;
    private String password;
    private String email;
    private String profession;
    private BigDecimal salary;
    private int activeAccounts = 0;

    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;


}
