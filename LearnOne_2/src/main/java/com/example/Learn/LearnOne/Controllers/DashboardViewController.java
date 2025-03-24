package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Services.DashboardViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardViewController {

    @Autowired
    private DashboardViewService dashboardService;

    @GetMapping("/dashboardView")
    public String viewDashboard(Model model) {
        int totalVoters = dashboardService.getTotalVoters();
        int[] ageDistribution = dashboardService.getAgeDistribution();
        int[] genderDistribution = dashboardService.getGenderDistribution();
        Map<String, Long> wardDistribution = dashboardService.getWardDistribution();
        List<Voter> voters = dashboardService.getVoterList();

        System.out.println("Total Voters: " + totalVoters);
        System.out.println("Age Distribution: " + Arrays.toString(ageDistribution));
        System.out.println("Gender Distribution: " + Arrays.toString(genderDistribution));
        System.out.println("Ward Distribution: " + wardDistribution);
        System.out.println("Voters: " + voters);

        model.addAttribute("totalVoters", totalVoters);
        model.addAttribute("ageDistribution", ageDistribution);
        model.addAttribute("genderDistribution", genderDistribution);
        model.addAttribute("wardDistribution", wardDistribution);
        model.addAttribute("voters", voters);

        return "dashboardView";
    }

}