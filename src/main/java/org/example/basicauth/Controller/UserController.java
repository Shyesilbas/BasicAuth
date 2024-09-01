package org.example.basicauth.Controller;


import lombok.RequiredArgsConstructor;
import org.example.basicauth.Model.User;
import org.example.basicauth.Repository.CurrentUser;
import org.example.basicauth.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String userProfile() {
        return "Welcome to your Profile!";
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@CurrentUser UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Unauthorized: No user logged in");
        }

        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(user);
    }
}
