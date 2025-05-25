package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.CommunityIssue;
import com.example.Learn.LearnOne.Entity.Users;
import com.example.Learn.LearnOne.Repository.CommunityIssueRepository;
import com.example.Learn.LearnOne.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommunityIssueService {

    private final CommunityIssueRepository communityIssueRepository;
    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public CommunityIssueService(CommunityIssueRepository communityIssueRepository, UserRepository userRepository) {
        this.communityIssueRepository = communityIssueRepository;
        this.userRepository = userRepository;
        // Create uploads directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory", e);
        }
    }

    public CommunityIssue submitIssue(Long userId, String issueTitle, String issueDescription,
                                      CommunityIssue.IssueCategory category, String location,
                                      CommunityIssue.IssuePriority priority, MultipartFile image) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = saveImage(image);
        }

        CommunityIssue issue = CommunityIssue.builder()
                .user(user)
                .issueTitle(issueTitle)
                .issueDescription(issueDescription)
                .category(category)
                .location(location)
                .priority(priority)
                .status(CommunityIssue.IssueStatus.PENDING)
                .submittedAt(LocalDateTime.now())
                .imageUrl(imageUrl)
                .build();

        return communityIssueRepository.save(issue);
    }

    private String saveImage(MultipartFile image) {
        try {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.write(filePath, image.getBytes());
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
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
                                      String assignedTo, MultipartFile image) {
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

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            issue.setImageUrl(imageUrl);
        }

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
        CommunityIssue issue = communityIssueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));
        if (issue.getImageUrl() != null) {
            try {
                Files.deleteIfExists(Paths.get("src/main/resources/static" + issue.getImageUrl()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image", e);
            }
        }
        communityIssueRepository.deleteById(issueId);
    }
}