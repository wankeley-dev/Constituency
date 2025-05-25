package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Users;
import com.example.Learn.LearnOne.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

public class AuthController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    // Registration Endpoints
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute Users user, RedirectAttributes redirectAttributes) {
        logger.info("Registration attempt - User data:");
        logger.info("Full Name: " + user.getFullName());
        logger.info("Email: " + user.getEmail());
        logger.info("Password: " + (user.getPassword() != null ? "*****" : "NULL"));
        logger.info("Role: " + user.getRole());

        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful. Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "redirect:/register";
        }
    }

    // Login Endpoints
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("errorMessage", null);
        return "login";
    }


}
