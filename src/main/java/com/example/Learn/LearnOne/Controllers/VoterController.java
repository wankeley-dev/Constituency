package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Users;
import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Services.UserService;
import com.example.Learn.LearnOne.Services.VoterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/voterInput")
public class VoterController {

    @Autowired
    private VoterService voterService;

    @Autowired
    private UserService userService;


    private static final Logger logger = LoggerFactory.getLogger(VoterController.class);


    @GetMapping("/VoterInput")
    public String showVoterForm(Model model) {
        model.addAttribute("voter", new Voter());
        model.addAttribute("employmentStatus", Voter.EmploymentStatus.values());
        return "Voter/voterInput"; // No need to pass users list unless you want to optionally link
    }

    @PostMapping("/save")
    public String saveVoter(@Valid @ModelAttribute("voter") Voter voter, BindingResult result,

                           RedirectAttributes redirectAttributes, Model model) {
        /*
        if (result.hasErrors()) {
            model.addAttribute("employmentStatus", Voter.EmploymentStatus.values());
            logger.info("Error from employment status");
            return "Voter/voterInput";
        }

         */

        if (voterService.getVoter(voter.getVoterId()).isPresent() && voter.getId() == null) {
            result.rejectValue("voterId", "error.voterId", "Voter ID already exists.");
            model.addAttribute("employmentStatus", Voter.EmploymentStatus.values());
            return "Voter/voterInput";
        }

        voterService.saveVoter(voter);
        redirectAttributes.addFlashAttribute("message", "Voter information saved successfully!");
        return "redirect:/voterInput/view";
    }

    @GetMapping("/view")
    public String viewVoterInformation(Model model,
                                       @RequestParam(required = false) String voterId,
                                       @RequestParam(required = false) String branch,
                                       @RequestParam(required = false) String pollingStation,
                                       @RequestParam(required = false) Boolean active,
                                       @RequestParam(required = false) String keyword) {
        List<Voter> voters = voterService.filterVoters(voterId, branch, pollingStation, active, keyword);
        model.addAttribute("voters", voters);
        model.addAttribute("voterId", voterId);
        model.addAttribute("branch", branch);
        model.addAttribute("pollingStation", pollingStation);
        model.addAttribute("active", active);
        model.addAttribute("keyword", keyword);
        return "Voter/voterView";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Voter> voter = voterService.getVoterById(id);
        if (voter.isPresent()) {
            model.addAttribute("voter", voter.get());
            model.addAttribute("users", userService.findAllUsers());
            return "Voter/voterInput";
        } else {
            redirectAttributes.addFlashAttribute("message", "Voter not found!");
            return "redirect:/voterInput/view";
        }
    }

    @GetMapping("/VoterDetails/{id}")
    public String showVoterDetailsForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Voter> voter = voterService.getVoterById(id);
        if (voter.isPresent()) {
            model.addAttribute("voter", voter.get());
            logger.info("Voter details found and added");
            return "Voter/voterDetails";
        } else {
            redirectAttributes.addFlashAttribute("error", "Voter not found!");
            return "redirect:/voterInput/view";
        }
    }

    @PostMapping("/update")
    public String updateVoter(@Valid @ModelAttribute("voter") Voter voter, BindingResult result,
                              RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAllUsers());
            return "Voter/voterInput";
        }
        voterService.updateVoter(voter);
        redirectAttributes.addFlashAttribute("message", "Voter information updated successfully!");
        return "redirect:/voterInput/view";
    }

    @GetMapping("/delete/{id}")
    public String deleteVoter(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        voterService.deleteVoter(id);
        redirectAttributes.addFlashAttribute("message", "Voter deleted successfully!");
        return "redirect:/voterInput/view";
    }

    @GetMapping("/user/{userId}")
    public String getVoterByUser(@PathVariable Long userId, Model model, RedirectAttributes redirectAttributes) {
        Users user = userService.getUserById(userId) // Assume this method exists
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Voter> voter = voterService.getVoterByUser(user);
        if (voter.isPresent()) {
            model.addAttribute("voter", voter.get());
            return "Voter/voterDetail";
        } else {
            redirectAttributes.addFlashAttribute("message", "Voter not found for the given user.");
            return "redirect:/voterInput/view";
        }
    }
}
