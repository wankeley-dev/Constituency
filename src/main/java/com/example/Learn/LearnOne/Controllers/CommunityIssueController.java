package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.CommunityIssue;
import com.example.Learn.LearnOne.Entity.Users;
import com.example.Learn.LearnOne.Services.CommunityIssueService;
import com.example.Learn.LearnOne.Services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/issues")
public class CommunityIssueController {

    private final CommunityIssueService communityIssueService;
    private final UserService userService;

    public CommunityIssueController(CommunityIssueService communityIssueService, UserService userService) {
        this.communityIssueService = communityIssueService;
        this.userService = userService;
    }

    @GetMapping
    public String showIssuesPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Users> loggedInUser = userService.findByEmail(userDetails.getUsername());
        Long userId = loggedInUser.map(Users::getId).orElse(null);

        List<CommunityIssue> issues = communityIssueService.getAllIssues();
        model.addAttribute("issues", issues);
        model.addAttribute("userId", userId);
        model.addAttribute("categories", CommunityIssue.IssueCategory.values());
        model.addAttribute("priorities", CommunityIssue.IssuePriority.values());
        model.addAttribute("statuses", CommunityIssue.IssueStatus.values());

        return "CommunityIssue/community-issues";
    }

    @PostMapping("/submit")
    public String submitIssue(@RequestParam Long userId, @RequestParam String issueTitle,
                              @RequestParam("description") String description,
                              @RequestParam CommunityIssue.IssueCategory category,
                              @RequestParam String location,
                              @RequestParam CommunityIssue.IssuePriority priority,
                              @RequestParam(value = "image", required = false) MultipartFile image) {
        communityIssueService.submitIssue(userId, issueTitle, description, category, location, priority, image);
        return "redirect:/issues";
    }

    @GetMapping("/edit/{issueId}")
    public String showEditIssueForm(@PathVariable Long issueId, Model model) {
        CommunityIssue issue = communityIssueService.getIssueById(issueId).orElse(null);
        model.addAttribute("issue", issue);
        model.addAttribute("issues", communityIssueService.getAllIssues());
        model.addAttribute("categories", CommunityIssue.IssueCategory.values());
        model.addAttribute("priorities", CommunityIssue.IssuePriority.values());
        model.addAttribute("statuses", CommunityIssue.IssueStatus.values());
        return "CommunityIssue/community-issues";
    }

    @GetMapping("/update/{issueId}")
    public String showAdminEditIssueForm(@PathVariable Long issueId, Model model) {
        CommunityIssue issue = communityIssueService.getIssueById(issueId).orElse(null);
        model.addAttribute("issue", issue);
        model.addAttribute("issues", communityIssueService.getAllIssues());
        model.addAttribute("categories", CommunityIssue.IssueCategory.values());
        model.addAttribute("priorities", CommunityIssue.IssuePriority.values());
        model.addAttribute("statuses", CommunityIssue.IssueStatus.values());
        return "CommunityIssue/admin-community-issues";
    }



    @PostMapping("/update/{issueId}")
    public String updateIssue(@PathVariable Long issueId, @RequestParam String issueTitle,
                              @RequestParam("description") String description,
                              @RequestParam CommunityIssue.IssueCategory category,
                              @RequestParam String location,
                              @RequestParam CommunityIssue.IssuePriority priority,
                              @RequestParam CommunityIssue.IssueStatus status,
                              @RequestParam(required = false) String resolutionDetails,
                              @RequestParam(required = false) String assignedTo,
                              @RequestParam(value = "image", required = false) MultipartFile image) {
        communityIssueService.updateIssue(issueId, issueTitle, description, category, location, priority,
                status, resolutionDetails, assignedTo, image);
        return "redirect:/issues";
    }

    @GetMapping("/delete/{issueId}")
    public String deleteIssue(@PathVariable Long issueId) {
        communityIssueService.deleteIssue(issueId);
        return "redirect:/issues";
    }

    @GetMapping("/viewIssues")
    public String viewIssues(Model model) {
        model.addAttribute("issues", communityIssueService.getAllIssues());
        return "/CommunityIssue/viewIssue";
    }
}