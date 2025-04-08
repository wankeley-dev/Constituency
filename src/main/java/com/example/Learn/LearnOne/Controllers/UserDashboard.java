package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Users;
import jakarta.persistence.Column;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserDashboard {

    @GetMapping("/user-dashboard")
    public String showUserDashboard(Model model, @AuthenticationPrincipal Users user) {
        if (user != null) {
            model.addAttribute("user", user); // Add the authenticated user to the model
        } else {
            // Fallback if no user is authenticated (unlikely with proper security config)
            model.addAttribute("user", new Users()); // Default empty user
            model.addAttribute("error", "User not found. Please log in again.");
        }
        return "Dashboard/userDashboard";
    }
}
