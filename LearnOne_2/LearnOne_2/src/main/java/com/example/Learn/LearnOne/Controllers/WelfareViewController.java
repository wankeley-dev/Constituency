package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Welfare;
import com.example.Learn.LearnOne.Services.WelfareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/welfareView")
public class WelfareViewController {

    @Autowired
    private WelfareService welfareService;

    @GetMapping
    public String viewWelfare(Model model) {
        List<Welfare> welfares = welfareService.findAllWelfares();
        model.addAttribute("welfares", welfares);
        return "welfareView"; // Ensure this matches your Thymeleaf template
    }
}