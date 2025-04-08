package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Services.adminDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
            Map<String, Integer> ageDistribution = dashboardService.getAgeDistribution();
            Map<String, Integer> genderDistribution = dashboardService.getGenderDistribution();
            Map<String, Integer> employmentDistribution = dashboardService.getEmploymentDistribution();

            // Ward and welfare data
            Map<String, Long> wardDistribution = dashboardService.getWardDistribution();
            Map<String, Long> welfareCategoryDistribution = dashboardService.getWelfareCategoryDistribution();

            // Event participation data
            Map<String, Integer> eventParticipation = dashboardService.getEventParticipation();
            Map<String, Integer> eventParticipationByWard = dashboardService.getEventParticipationByWard();

            // Additional data
            List<Voter> voters = dashboardService.getVoterList();
            long totalWelfareBeneficiaries = dashboardService.getTotalWelfareBeneficiaries();
            int totalEventParticipants = dashboardService.getTotalEventParticipants();

            // Debug the exact maps
            // Log the exact data for debugging
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
            logger.info("Ward Distribution Map: {}", wardDistribution);
            logger.info("Event Participation by Ward Map: {}", eventParticipationByWard);

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
            logger.error("Error occurred while fetching dashboard data: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Failed to load dashboard data. Please try again later.");
        }

        logger.info("Returning admin-dashboard view");
        return "Dashboard/admin-dashboard";
    }
}
