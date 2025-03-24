package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Services.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/voterView")
public class VoterViewController {

    @Autowired
    private VoterService voterService;

    @GetMapping
    public String viewVoters(Model model) {
        List<Voter> voters = voterService.findAllVoters();
        model.addAttribute("voters", voters);
        return "voterView"; // Ensure this matches your Thymeleaf template
    }
}