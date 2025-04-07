package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.CommunityIssue;
import com.example.Learn.LearnOne.Entity.Users;
import com.example.Learn.LearnOne.Repository.CommunityIssueRepository;
import com.example.Learn.LearnOne.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommunityIssueService {

    private final CommunityIssueRepository communityIssueRepository;
    private final UserRepository userRepository;

    public CommunityIssueService(CommunityIssueRepository communityIssueRepository, UserRepository userRepository) {
        this.communityIssueRepository = communityIssueRepository;
        this.userRepository = userRepository;
    }

    public CommunityIssue submitIssue(Long userId, String issueTitle, String issueDescription,
                                      CommunityIssue.IssueCategory category, String location,
                                      CommunityIssue.IssuePriority priority) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        CommunityIssue issue = CommunityIssue.builder()
                .user(user)
                .issueTitle(issueTitle)
                .issueDescription(issueDescription)
                .category(category)
                .location(location)
                .priority(priority)
                .status(CommunityIssue.IssueStatus.PENDING)
                .submittedAt(LocalDateTime.now())
                .build();

        return communityIssueRepository.save(issue);
    }

    public List<CommunityIssue> getUserIssues(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return communityIssueRepository.findByUser(user);
    }

    public List<CommunityIssue> getIssuesByStatus(CommunityIssue.IssueStatus status) {
        return communityIssueRepository.findByStatus(status);
    }

    public List<CommunityIssue> getIssuesByCategory(CommunityIssue.IssueCategory category) {
        return communityIssueRepository.findByCategory(category);
    }

    public List<CommunityIssue> getIssuesByPriority(CommunityIssue.IssuePriority priority) {
        return communityIssueRepository.findByPriority(priority);
    }

    public Optional<CommunityIssue> getIssueById(Long issueId) {
        return communityIssueRepository.findById(issueId);
    }

    public CommunityIssue updateIssue(Long issueId, String issueTitle, String issueDescription,
                                      CommunityIssue.IssueCategory category, String location,
                                      CommunityIssue.IssuePriority priority,
                                      CommunityIssue.IssueStatus status, String resolutionDetails,
                                      String assignedTo) {
        CommunityIssue issue = communityIssueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        issue.setIssueTitle(issueTitle);
        issue.setIssueDescription(issueDescription);
        issue.setCategory(category);
        issue.setLocation(location);
        issue.setPriority(priority);
        issue.setStatus(status);
        issue.setUpdatedAt(LocalDateTime.now());
        issue.setAssignedTo(assignedTo);

        if (status == CommunityIssue.IssueStatus.RESOLVED) {
            issue.setResolvedAt(LocalDateTime.now());
            issue.setResolutionDetails(resolutionDetails);
        }

        return communityIssueRepository.save(issue);
    }

    public List<CommunityIssue> getAllIssues() {
        return communityIssueRepository.findAll();
    }

    public void deleteIssue(Long issueId) {
        communityIssueRepository.deleteById(issueId);
    }
}