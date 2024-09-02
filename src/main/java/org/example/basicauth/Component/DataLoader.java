package org.example.basicauth.Component;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.Customer;
import org.example.basicauth.Model.Role;
import org.example.basicauth.Model.User;
import org.example.basicauth.Repository.CustomerRepository;
import org.example.basicauth.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User(null, "admin", passwordEncoder.encode("admin123"), Role.ADMIN);
            User user = new User(null, "user", passwordEncoder.encode("user123"), Role.USER);
            Customer customer = new Customer(null,3593L,"customer" ,passwordEncoder.encode("customer123"),"shyesilbas@gmail.com","Engineer",new BigDecimal("50000.00"),1,Role.CUSTOMER);

            userRepository.save(admin);
            userRepository.save(user);
            customerRepository.save(customer);
        }
    }
}
