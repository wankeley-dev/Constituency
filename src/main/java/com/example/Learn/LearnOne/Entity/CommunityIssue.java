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
    private Users user;

    @Column(nullable = false)
    private String issueTitle;

    @Column(nullable = false, length = 100)
    private String issueDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueCategory category;

    @Column
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssuePriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueStatus status;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;

    @Column(length = 1000)
    private String resolutionDetails;

    private String assignedTo;

    @Column
    private String imageUrl; // New field for image path

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