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
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Welcome to the Admin Dashboard!";
    }
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentAdmin(@CurrentUser UserDetails userDetails){
        if (userDetails == null) {
            throw new RuntimeException("Unauthorized: No user logged in");
        }

        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(user);
    }


}
