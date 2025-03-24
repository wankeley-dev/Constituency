package com.example.Learn.LearnOne.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @NotBlank(message = "Voter ID is required")
    @Size(min = 10, max = 10, message = "Voter ID must be exactly 10 characters")
    private String voterId;

    @Positive(message = "Age must be positive")
    private int age;

    private String occupation;

    @NotBlank(message = "Branch is required")
    private String branch;

    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    private String phone;

    private String gender;

    @NotBlank(message = "Address is required")
    private String address;

    @PastOrPresent(message = "Registration date cannot be in the future")
    private LocalDate registrationDate;

    @NotBlank(message = "Polling station is required")
    private String pollingStation;

    private boolean active = true; // Default to active voter

    @OneToMany(mappedBy = "voter")
    private List<Welfare> welfares;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getVoterId() { return voterId; }
    public void setVoterId(String voterId) { this.voterId = voterId; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public String getPollingStation() { return pollingStation; }
    public void setPollingStation(String pollingStation) { this.pollingStation = pollingStation; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}