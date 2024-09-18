package org.example.basicauth.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.example.basicauth.Component.CustomLogoutSuccessHandler;
import org.example.basicauth.Jwt.JwtUtil;
import org.example.basicauth.Model.Customer;
import org.example.basicauth.Model.User;
import org.example.basicauth.Repository.CustomerRepository;
import org.example.basicauth.Repository.UserRepository;
import org.example.basicauth.Service.CombinedUserDetailsService;
import org.example.basicauth.Service.CustomerDetailsServiceImpl;
import org.example.basicauth.Service.TokenService;
import org.example.basicauth.Service.UserDetailsServiceImpl;
import org.example.basicauth.dto.AuthRequest;
import org.example.basicauth.dto.AuthResponse;
import org.example.basicauth.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CombinedUserDetailsService combinedUserDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final TokenService tokenService;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (customerRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Customer EXISTS");
        }
        if(customerRepository.findByPersonalId(request.getPersonalId()).isPresent()){
            return ResponseEntity.badRequest().body("Customer exists!");
        }

        Customer customer = Customer.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .profession(request.getProfession())
                .salary(request.getSalary())
                .personalId(request.getPersonalId())
                .build();

        customerRepository.save(customer);
        return ResponseEntity.ok("Customer register successful");
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new AuthResponse("Bad Credentials or you are not authorized"));
        }
        if (tokenService.hasActiveToken(request.getUsername())) {
            return ResponseEntity.status(403).body(new AuthResponse("You have already an active session."));
        }
        UserDetails userDetails = combinedUserDetailsService.loadUserByUsername(request.getUsername());

        final int expiryHours = 10;
        final String jwt = jwtUtil.generateToken(userDetails,expiryHours);
        tokenService.saveToken(jwt,expiryHours, userDetails.getUsername());
        return ResponseEntity.ok(new AuthResponse( STR."Welcome Back , \{request.getUsername()}"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        customLogoutSuccessHandler.logout(request, response, authentication);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Logout successful");
        return ResponseEntity.ok(responseBody);
    }


    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}



