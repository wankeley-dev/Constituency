package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Entity.Welfare;
import com.example.Learn.LearnOne.Services.VoterService;
import com.example.Learn.LearnOne.Services.WelfareService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/welfareInput")
public class WelfareController {

    @Autowired
    private WelfareService welfareService;

    @Autowired
    private VoterService voterService; // Moved to class-level injection

    @GetMapping
    public String showWelfareForm(Model model) {
        model.addAttribute("welfare", new Welfare());
        return "welfareInput";
    }

    @PostMapping("/save")
    public String saveWelfare(@Valid @ModelAttribute("welfare") Welfare welfare, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "welfareInput";
        }
        Voter voter = voterService.getVoter(welfare.getVoter().getVoterId()).orElse(null);
        if (voter == null) {
            result.rejectValue("voter.voterId", "error.voter", "Voter ID does not exist.");
            return "welfareInput";
        }
        welfare.setVoter(voter);
        welfareService.saveOrUpdateWelfare(welfare);
        redirectAttributes.addFlashAttribute("message", "Welfare information saved successfully!");
        return "redirect:/welfareInput";
    }

    @GetMapping("/view")
    public String viewWelfareInformation(Model model) {
        model.addAttribute("welfares", welfareService.findAllWelfares());
        return "welfareView";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Welfare> welfare = welfareService.getWelfareById(id);
        if (welfare.isPresent()) {
            model.addAttribute("welfare", welfare.get());
            return "welfareInput";
        } else {
            redirectAttributes.addFlashAttribute("message", "Welfare record not found!");
            return "redirect:/welfareInput/view";
        }
    }

    @PostMapping("/update")
    public String updateWelfare(@Valid @ModelAttribute("welfare") Welfare welfare, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "welfareInput";
        }
        Voter voter = voterService.getVoter(welfare.getVoter().getVoterId()).orElse(null);
        if (voter == null) {
            result.rejectValue("voter.voterId", "error.voter", "Voter ID does not exist.");
            return "welfareInput";
        }
        welfare.setVoter(voter);
        welfareService.saveOrUpdateWelfare(welfare);
        redirectAttributes.addFlashAttribute("message", "Welfare information updated successfully!");
        return "redirect:/welfareInput/view";
    }

    @GetMapping("/delete/{id}")
    public String deleteWelfare(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        welfareService.deleteWelfare(id);
        redirectAttributes.addFlashAttribute("message", "Welfare record deleted successfully!");
        return "redirect:/welfareInput/view";
    }

    @GetMapping("/search")
    public String searchWelfares(@RequestParam("ward") String ward, Model model) {
        model.addAttribute("welfares", welfareService.findWelfaresByWard(ward));
        return "welfareView";
    }

    @GetMapping("/print")
    public String printWelfares(Model model) {
        model.addAttribute("welfares", welfareService.findAllWelfares());
        return "welfarePrint";
    }
}