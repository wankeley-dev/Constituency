package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Services.adminDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminDashboard {

    private static final Logger logger = LoggerFactory.getLogger(AdminDashboard.class);

    @Autowired
    private adminDashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboardRedirect() {
        logger.info("Redirecting from /dashboard to /admin-dashboard");
        return "redirect:/admin-dashboard";
    }

    @GetMapping("/admin-dashboard")
    public String viewDashboard(Model model) {
        logger.info("Entering viewDashboard method");

        try {
            logger.info("Fetching dashboard data...");

            // Voter statistics
            int totalVoters = dashboardService.getTotalVoters();
            Map<String, Integer> ageDistribution = dashboardService.getAgeDistribution() != null
                    ? dashboardService.getAgeDistribution() : new HashMap<>();
            Map<String, Integer> genderDistribution = dashboardService.getGenderDistribution() != null
                    ? dashboardService.getGenderDistribution() : new HashMap<>();
            Map<String, Integer> employmentDistribution = dashboardService.getEmploymentDistribution() != null
                    ? dashboardService.getEmploymentDistribution() : new HashMap<>();

            // Ward and welfare data
            Map<String, Integer> wardDistribution = new HashMap<>();
            wardDistribution.put("Ward1", dashboardService.getWelfareBeneficiariesByWard("Ward1"));
            wardDistribution.put("Ward2", dashboardService.getWelfareBeneficiariesByWard("Ward2"));
            wardDistribution.put("Ward3", dashboardService.getWelfareBeneficiariesByWard("Ward3"));
            wardDistribution.put("Ward4", dashboardService.getWelfareBeneficiariesByWard("Ward4"));

            Map<String, Integer> welfareCategoryDistribution = dashboardService.getWelfareCategoryDistribution() != null
                    ? dashboardService.getWelfareCategoryDistribution() : new HashMap<>();

            // Event participation data
            Map<String, Integer> eventParticipation = dashboardService.getEventParticipation() != null
                    ? dashboardService.getEventParticipation() : new HashMap<>();
            Map<String, Integer> eventParticipationByWard = new HashMap<>();
            eventParticipationByWard.put("Ward1", dashboardService.getEventParticipationByWard("Ward1"));
            eventParticipationByWard.put("Ward2", dashboardService.getEventParticipationByWard("Ward2"));
            eventParticipationByWard.put("Ward3", dashboardService.getEventParticipationByWard("Ward3"));
            eventParticipationByWard.put("Ward4", dashboardService.getEventParticipationByWard("Ward4"));

            // Additional data
            List<Voter> voters = dashboardService.getVoterList() != null
                    ? dashboardService.getVoterList() : List.of();
            long totalWelfareBeneficiaries = dashboardService.getTotalWelfareBeneficiaries();
            int totalEventParticipants = dashboardService.getTotalEventParticipants();

            // Log data for debugging
            logger.info("Total Voters: {}", totalVoters);
            logger.info("Age Distribution: {}", ageDistribution);
            logger.info("Gender Distribution: {}", genderDistribution);
            logger.info("Employment Distribution: {}", employmentDistribution);
            logger.info("Ward Distribution: {}", wardDistribution);
            logger.info("Welfare Category Distribution: {}", welfareCategoryDistribution);
            logger.info("Event Participation: {}", eventParticipation);
            logger.info("Event Participation by Ward: {}", eventParticipationByWard);
            logger.info("Total Welfare Beneficiaries: {}", totalWelfareBeneficiaries);
            logger.info("Total Event Participants: {}", totalEventParticipants);

            // Add attributes to model
            model.addAttribute("totalVoters", totalVoters);
            model.addAttribute("ageDistribution", ageDistribution);
            model.addAttribute("genderDistribution", genderDistribution);
            model.addAttribute("employmentDistribution", employmentDistribution);
            model.addAttribute("wardDistribution", wardDistribution);
            model.addAttribute("welfareCategoryDistribution", welfareCategoryDistribution);
            model.addAttribute("eventParticipation", eventParticipation);
            model.addAttribute("eventParticipationByWard", eventParticipationByWard);
            model.addAttribute("voters", voters);
            model.addAttribute("totalWelfareBeneficiaries", totalWelfareBeneficiaries);
            model.addAttribute("totalEventParticipants", totalEventParticipants);

            logger.info("All attributes added to model successfully");

        } catch (Exception e) {
            logger.error("Error fetching dashboard data: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Failed to load dashboard data. Please try again.");
            return "error";
        }

        logger.info("Returning adminDashboard view");
        return "/Dashboard/admin-dashboard";
    }
}