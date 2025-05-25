package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Entity.Welfare;
import com.example.Learn.LearnOne.Entity.WelfareAssistance;
import com.example.Learn.LearnOne.Services.VoterService;
import com.example.Learn.LearnOne.Services.WelfareService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/welfareInput")
public class WelfareController {

    @Autowired
    private WelfareService welfareService;

    @Autowired
    private VoterService voterService;

    @GetMapping("WelfareInput")
    public String showWelfareForm(Model model) {
        model.addAttribute("welfare", new Welfare());
        model.addAttribute("categories", Welfare.BeneficiaryCategory.values());
        model.addAttribute("voters", voterService.findAllVoters());
        return "Welfare/welfareInput";
    }

    @PostMapping("/save")
    public String saveWelfare(@Valid @ModelAttribute("welfare") Welfare welfare, BindingResult result,
                              RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Welfare.BeneficiaryCategory.values());
            model.addAttribute("voters", voterService.findAllVoters());
            return "Welfare/welfareInput";
        }
        Voter voter = voterService.getVoterById(welfare.getVoter().getId())
                .orElseThrow(() -> new IllegalArgumentException("Voter not found"));
        welfare.setVoter(voter);
        welfareService.saveOrUpdateWelfare(welfare);
        redirectAttributes.addFlashAttribute("message", "Welfare information saved successfully!");
        return "redirect:/welfareInput/view";
    }

    @GetMapping("/view")
    public String viewWelfareInformation(Model model,
                                         @RequestParam(required = false) String ward,
                                         @RequestParam(required = false) String paymentStatus,
                                         @RequestParam(required = false) Welfare.BeneficiaryCategory category,
                                         @RequestParam(required = false) String startDate,
                                         @RequestParam(required = false) String endDate) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
        List<Welfare> welfares = welfareService.filterWelfares(ward, paymentStatus, category, start, end);
        model.addAttribute("welfares", welfares);
        model.addAttribute("categories", Welfare.BeneficiaryCategory.values());
        model.addAttribute("ward", ward);
        model.addAttribute("paymentStatus", paymentStatus);
        model.addAttribute("category", category);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "Welfare/welfareView";
    }

    @GetMapping("/welfarePrint")
    public String printWelfare(Model model,
                               @RequestParam(required = false) String ward,
                               @RequestParam(required = false) String paymentStatus,
                               @RequestParam(required = false) Welfare.BeneficiaryCategory category,
                               @RequestParam(required = false) String startDate,
                               @RequestParam(required = false) String endDate) {
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;
        List<Welfare> welfares = welfareService.filterWelfares(ward, paymentStatus, category, start, end);
        model.addAttribute("welfares", welfares);
        return "Welfare/welfarePrint";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Welfare> welfare = welfareService.getWelfareById(id);
        if (welfare.isPresent()) {
            model.addAttribute("welfare", welfare.get());
            model.addAttribute("categories", Welfare.BeneficiaryCategory.values());
            model.addAttribute("voters", voterService.findAllVoters());
            return "Welfare/welfareInput";
        } else {
            redirectAttributes.addFlashAttribute("message", "Welfare record not found!");
            return "redirect:/welfareInput/view";
        }
    }

    @PostMapping("/update")
    public String updateWelfare(@Valid @ModelAttribute("welfare") Welfare welfare, BindingResult result,
                                RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Welfare.BeneficiaryCategory.values());
            model.addAttribute("voters", voterService.findAllVoters());
            return "Welfare/welfareInput";
        }
        Voter voter = voterService.getVoterById(welfare.getVoter().getId())
                .orElseThrow(() -> new IllegalArgumentException("Voter not found"));
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

    @GetMapping("/history/{id}")
    public String viewHistory(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Welfare> welfare = welfareService.getWelfareById(id);
        if (welfare.isPresent()) {
            model.addAttribute("welfare", welfare.get());
            model.addAttribute("assistance", new WelfareAssistance());
            return "Welfare/welfareHistory";
        } else {
            redirectAttributes.addFlashAttribute("message", "Welfare record not found!");
            return "redirect:/welfareInput/view";
        }
    }

    @PostMapping("/add-assistance/{id}")
    public String addAssistance(@PathVariable Long id, @ModelAttribute("assistance") WelfareAssistance assistance,
                                RedirectAttributes redirectAttributes) {
        welfareService.addAssistance(id, assistance);
        redirectAttributes.addFlashAttribute("message", "Assistance added successfully!");
        return "redirect:/welfareInput/history/" + id;
    }

    @GetMapping("/report")
    public String generateReport(Model model,
                                 @RequestParam(required = false) String startDate,
                                 @RequestParam(required = false) String endDate) {
        Map<String, Object> categoryReport = welfareService.generateCategoryReport();
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
        Map<String, Object> wardReport = welfareService.generateWardReport(start, end);
        model.addAttribute("categoryReport", categoryReport);
        model.addAttribute("wardReport", wardReport);
        return "Welfare/welfareReport";
    }
}