package org.example.basicauth.Service;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.Customer;
import org.example.basicauth.Repository.CustomerRepository;

import org.example.basicauth.dto.CreateCustomer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public Customer saveCustomer(CreateCustomer createCustomer){
        if(createCustomer == null){
            throw new NullPointerException("Request cannot be null");
        }
        if(customerRepository.findByUsername(createCustomer.getUsername()).isPresent()){
            throw new RuntimeException("User already exists");
        }
        if(customerRepository.findByEmail(createCustomer.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }
        Customer customer = new Customer();
        customer.setPersonalId(createCustomer.getPersonalId());
        customer.setUsername(createCustomer.getUsername());
        customer.setEmail(createCustomer.getEmail());
        customer.setPassword(passwordEncoder.encode(createCustomer.getPassword()));
        customer.setSalary(createCustomer.getSalary());
        customer.setProfession(createCustomer.getProfession());

        return customerRepository.save(customer);
    }
    public Customer getCustomer(String username){
        return customerRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

}
