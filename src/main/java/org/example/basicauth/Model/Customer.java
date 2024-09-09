package org.example.basicauth.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Customer")
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private Long personalId;

    @Column(unique = true)
    private String username;

    private String password;
    private String email;
    private String profession;
    private BigDecimal salary;
    private int activeAccounts = 0;
    private int activeLoans = 0;
    private int activeCashAdv = 0;


    @Column(name = "activeCC")
    private int activeCreditCards = 0;

    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;


}
