package org.example.basicauth.Controller;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.Customer;
import org.example.basicauth.Model.User;
import org.example.basicauth.Repository.CurrentUser;
import org.example.basicauth.Repository.CustomerRepository;
import org.example.basicauth.Service.CustomerService;
import org.example.basicauth.Service.UserService;
import org.example.basicauth.dto.CreateCustomer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Welcome to the Admin Dashboard!";
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentAdmin(@CurrentUser UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Unauthorized: No user logged in");
        }
        User user = userService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/allCustomers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<Customer> saveCustomer(@RequestBody CreateCustomer createCustomer) {
        try {
            Customer customer = customerService.saveCustomer(createCustomer);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
