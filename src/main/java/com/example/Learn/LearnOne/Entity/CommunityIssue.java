package com.example.Learn.LearnOne.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; // The user who submitted the issue

    @Column(nullable = false)
    private String issueTitle; // Title of the issue

    @Column(nullable = false, length = 1000)
    private String issueDescription; // Detailed description of the issue

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueCategory category; // Category of the issue (e.g., Infrastructure, Safety)

    @Column
    private String location; // Specific location of the issue (e.g., street name, coordinates)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssuePriority priority; // Priority level (e.g., Low, Medium, High)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueStatus status; // Status of the issue

    @Column(nullable = false)
    private LocalDateTime submittedAt; // When the issue was submitted

    private LocalDateTime updatedAt; // Last updated timestamp
    private LocalDateTime resolvedAt; // When the issue was resolved

    @Column(length = 1000)
    private String resolutionDetails; // Details of how the issue was resolved

    private String assignedTo; // Person or team assigned to handle the issue

    // Enums for structured data
    public enum IssueStatus {
        PENDING, IN_PROGRESS, RESOLVED, REJECTED, ON_HOLD
    }

    public enum IssueCategory {
        INFRASTRUCTURE, SAFETY, ENVIRONMENT, PUBLIC_SERVICES, OTHER
    }

    public enum IssuePriority {
        LOW, MEDIUM, HIGH, URGENT
    }
}