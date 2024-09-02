package org.example.basicauth.Controller;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.Customer;
import org.example.basicauth.Model.User;
import org.example.basicauth.Repository.CurrentUser;
import org.example.basicauth.Service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    @GetMapping("/me")
    public ResponseEntity<Customer> getCurrentUser(@CurrentUser UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Unauthorized: No user logged in");
        }

        Customer customer = customerService.getCustomer(userDetails.getUsername());
        return ResponseEntity.ok(customer);
    }

}
