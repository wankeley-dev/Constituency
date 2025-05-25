package com.example.Learn.LearnOne;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "redirect:/login";  // Or redirect to another page likedfgf "/dashboard"
    }
}
