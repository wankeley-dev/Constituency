package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.User;
import com.example.Learn.LearnOne.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Ensure this matches your Thymeleaf template name for the login page
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {
        User user = userService.authenticate(email, password);
        if (user != null) {
            // Redirect to the dashboard if successful
            return "redirect:/dashboard"; // Update with your dashboard URL
        } else {
            // Show error message if unsuccessful
            model.addAttribute("errorMessage", "Invalid email or password. Please try again.");
            return "login"; // Update this to your login view
        }
    }
}
