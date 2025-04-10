package com.example.Learn.LearnOne.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String phoneNumber;
    private String address;
    private String constituency;
    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER, ADMIN
        //WELFARE_OFFICER, COMMUNITY_LEADER, CONSTITUENT
    }

    private String issueDescription;

    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;

    private LocalDateTime submittedAt;

    public enum IssueStatus {
        PENDING, RESOLVED, IN_PROGRESS
    }

    private boolean volunteer = false; // Renamed from isVolunteer
    private String donationType;
    private LocalDateTime donationDate;

    private boolean welfareBeneficiary = false; // Renamed from isWelfareBeneficiary
    private LocalDateTime lastWelfareSupportDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Welfare> welfareRequests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Voter> voter;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CommunityIssue> communityIssues;

    // Explicit getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getConstituency() { return constituency; }
    public void setConstituency(String constituency) { this.constituency = constituency; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getIssueDescription() { return issueDescription; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }

    public IssueStatus getIssueStatus() { return issueStatus; }
    public void setIssueStatus(IssueStatus issueStatus) { this.issueStatus = issueStatus; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public boolean getVolunteer() { return volunteer; } // Changed to getVolunteer
    public void setVolunteer(boolean volunteer) { this.volunteer = volunteer; }

    public String getDonationType() { return donationType; }
    public void setDonationType(String donationType) { this.donationType = donationType; }

    public LocalDateTime getDonationDate() { return donationDate; }
    public void setDonationDate(LocalDateTime donationDate) { this.donationDate = donationDate; }

    public boolean getWelfareBeneficiary() { return welfareBeneficiary; } // Changed to getWelfareBeneficiary
    public void setWelfareBeneficiary(boolean welfareBeneficiary) { this.welfareBeneficiary = welfareBeneficiary; }

    public LocalDateTime getLastWelfareSupportDate() { return lastWelfareSupportDate; }
    public void setLastWelfareSupportDate(LocalDateTime lastWelfareSupportDate) { this.lastWelfareSupportDate = lastWelfareSupportDate; }

    public List<Welfare> getWelfareRequests() { return welfareRequests; }
    public void setWelfareRequests(List<Welfare> welfareRequests) { this.welfareRequests = welfareRequests; }

    public List<Voter> getVoter() { return voter; }
    public void setVoter(List<Voter> voter) { this.voter = voter; }

    public List<CommunityIssue> getCommunityIssues() { return communityIssues; }
    public void setCommunityIssues(List<CommunityIssue> communityIssues) { this.communityIssues = communityIssues; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
