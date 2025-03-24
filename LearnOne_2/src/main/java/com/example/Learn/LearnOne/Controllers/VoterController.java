package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Services.VoterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/voterInput")
public class VoterController {

    @Autowired
    private VoterService voterService;

    @GetMapping
    public String showVoterForm(Model model) {
        model.addAttribute("voter", new Voter());
        return "voterInput";
    }

    @PostMapping("/save")
    public String saveVoter(@Valid @ModelAttribute("voter") Voter voter, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "voterInput";
        }

        if (voterService.getVoter(voter.getVoterId()).isPresent() && voter.getId() == null) {
            result.rejectValue("voterId", "error.voterId", "Voter ID already exists.");
            return "voterInput";
        }

        if (voter.getRegistrationDate() == null) {
            voter.setRegistrationDate(LocalDate.now());
        }
        voterService.saveVoter(voter);
        redirectAttributes.addFlashAttribute("message", "Voter information saved successfully!");
        return "redirect:/voterInput";
    }

    @GetMapping("/view")
    public String viewVoterInformation(Model model) {
        model.addAttribute("voters", voterService.findAllVoters());
        return "voterView";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Voter> voter = voterService.findAllVoters().stream().filter(v -> v.getId().equals(id)).findFirst();
        if (voter.isPresent()) {
            model.addAttribute("voter", voter.get());
            return "voterInput";
        } else {
            redirectAttributes.addFlashAttribute("message", "Voter not found!");
            return "redirect:/voterInput/view";
        }
    }

    @PostMapping("/update")
    public String updateVoter(@Valid @ModelAttribute("voter") Voter voter, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "voterInput";
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

    @GetMapping("/search")
    public String searchVoters(@RequestParam("name") String name, Model model) {
        model.addAttribute("voters", voterService.searchVotersByName(name));
        return "voterView";
    }

    @GetMapping("/print")
    public String printVoters(Model model) {
        model.addAttribute("voters", voterService.findAllVoters());
        return "voterPrint"; // New template for printing
    }
}