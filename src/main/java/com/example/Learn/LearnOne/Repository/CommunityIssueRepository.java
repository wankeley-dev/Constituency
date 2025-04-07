package com.example.Learn.LearnOne.Repository;

import com.example.Learn.LearnOne.Entity.CommunityIssue;
import com.example.Learn.LearnOne.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityIssueRepository extends JpaRepository<CommunityIssue, Long> {
    List<CommunityIssue> findByUser(Users user);
    List<CommunityIssue> findByStatus(CommunityIssue.IssueStatus status);
    List<CommunityIssue> findByCategory(CommunityIssue.IssueCategory category);
    List<CommunityIssue> findByPriority(CommunityIssue.IssuePriority priority);
    List<CommunityIssue> findByLocationContainingIgnoreCase(String location);
    List<CommunityIssue> findByStatusAndUser(CommunityIssue.IssueStatus status, Users user);
}