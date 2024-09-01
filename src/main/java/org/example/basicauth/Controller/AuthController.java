package org.example.basicauth.Controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.example.basicauth.Jwt.JwtUtil;
import org.example.basicauth.Model.Role;
import org.example.basicauth.Model.User;
import org.example.basicauth.Repository.UserRepository;
import org.example.basicauth.Service.TokenService;
import org.example.basicauth.Service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("USERNAME EXISTS");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("register successful");
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
            return ResponseEntity.status(401).body(new AuthResponse(null, "Bad Credentials or you are not authorized"));
        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getUsername());
        final int expiryHours = 10;
        final String jwt = jwtUtil.generateToken(userDetails,expiryHours);
        tokenService.saveToken(jwt,expiryHours);



        return ResponseEntity.ok(new AuthResponse(jwt, "Login Successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = extractToken(request);

        // Log token
        System.out.println("Extracted token: " + token);

        // Get the current authentication
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            System.out.println("Authenticated username: " + username);

            // Check if the token is valid and matches the logged-in user
            if (token != null && tokenService.isTokenValid(token, username)) {
                tokenService.invalidateToken(token);
                SecurityContextHolder.clearContext();
                request.getSession().invalidate();
                return ResponseEntity.ok("Successfully logged out");
            }
        }

        return ResponseEntity.status(401).body("No active session found or token is missing");
    }


    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class AuthRequest {
    private String username;
    private String password;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class AuthResponse {
    private String token;
    private String message;
}
