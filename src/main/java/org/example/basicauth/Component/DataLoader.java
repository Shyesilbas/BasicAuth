package org.example.basicauth.Component;

import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.Role;
import org.example.basicauth.Model.User;
import org.example.basicauth.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User(null, "admin", passwordEncoder.encode("admin123"), Role.ADMIN);
            User user = new User(null, "user", passwordEncoder.encode("user123"), Role.USER);

            userRepository.save(admin);
            userRepository.save(user);
        }
    }
}
