package org.example.basicauth.Service;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.Customer;
import org.example.basicauth.Repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDetailsServiceImpl implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Customer not found"));


        return org.springframework.security.core.userdetails.User
                .withUsername(customer.getUsername())
                .password(customer.getPassword())
                .authorities(customer.getRole().name())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
