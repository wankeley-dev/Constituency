package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Users;
import com.example.Learn.LearnOne.Services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUsers(Model model,
                            @RequestParam(required = false) String email,
                            @RequestParam(required = false) Users.Role role,
                            @RequestParam(required = false) String constituency,
                            @RequestParam(required = false) Boolean volunteer, // Updated parameter name
                            @RequestParam(required = false) Boolean welfareBeneficiary, // Updated parameter name
                            @RequestParam(required = false) String keyword) {
        List<Users> users = userService.filterUsers(email, role, constituency, volunteer, welfareBeneficiary, keyword);
        model.addAttribute("users", users);
        model.addAttribute("roles", Users.Role.values());
        model.addAttribute("email", email);
        model.addAttribute("role", role);
        model.addAttribute("constituency", constituency);
        model.addAttribute("volunteer", volunteer); // Updated attribute name
        model.addAttribute("welfareBeneficiary", welfareBeneficiary); // Updated attribute name
        model.addAttribute("keyword", keyword);
        return "Users/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Users> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("roles", Users.Role.values());
            return "/Users/userEdit";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
            return "redirect:/users";
        }
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") Users user, BindingResult result,
                             RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", Users.Role.values());
            return "/Users/userEdit";
        }
        userService.updateUser(user.getId(), user);
        redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/users";
    }

    @PostMapping("/submit-complaint")
    public String submitComplaint(@RequestParam String email, @RequestParam String issueDescription,
                                  RedirectAttributes redirectAttributes) {
        Optional<Users> user = userService.findByEmail(email);
        if (user.isPresent()) {
            Users existingUser = user.get();
            existingUser.setIssueDescription(issueDescription);
            existingUser.setIssueStatus(Users.IssueStatus.PENDING);
            existingUser.setSubmittedAt(LocalDateTime.now());
            userService.updateUser(existingUser.getId(), existingUser);
            redirectAttributes.addFlashAttribute("successMessage", "Complaint submitted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
        }
        return "redirect:/users";
    }

    @PostMapping("/volunteer")
    public String registerVolunteer(@RequestParam String email, RedirectAttributes redirectAttributes) {
        Optional<Users> user = userService.findByEmail(email);
        if (user.isPresent()) {
            Users existingUser = user.get();
            existingUser.setVolunteer(true);
            userService.updateUser(existingUser.getId(), existingUser);
            redirectAttributes.addFlashAttribute("successMessage", "Thank you for volunteering!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
        }
        return "redirect:/users";
    }

    @PostMapping("/donate")
    public String recordDonation(@RequestParam String email, @RequestParam String donationType,
                                 RedirectAttributes redirectAttributes) {
        Optional<Users> user = userService.findByEmail(email);
        if (user.isPresent()) {
            Users existingUser = user.get();
            existingUser.setDonationType(donationType);
            existingUser.setDonationDate(LocalDateTime.now());
            userService.updateUser(existingUser.getId(), existingUser);
            redirectAttributes.addFlashAttribute("successMessage", "Thank you for your donation!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
        }
        return "redirect:/users";
    }

    @PostMapping("/mark-welfare-beneficiary")
    public String markAsWelfareBeneficiary(@RequestParam String email, RedirectAttributes redirectAttributes) {
        Optional<Users> user = userService.findByEmail(email);
        if (user.isPresent()) {
            Users existingUser = user.get();
            existingUser.setWelfareBeneficiary(true);
            existingUser.setLastWelfareSupportDate(LocalDateTime.now());
            userService.updateUser(existingUser.getId(), existingUser);
            redirectAttributes.addFlashAttribute("successMessage", "User marked as a welfare beneficiary.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
        }
        return "redirect:/users";
    }
}
