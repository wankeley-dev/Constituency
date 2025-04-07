package com.example.Learn.LearnOne.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // Nullable by default; set to false if every Voter must have a User
    private Users user;

    @NotBlank(message = "Full name is required")
    private String fullName; // Separate field for voter's name

    @NotBlank(message = "Voter ID is required")
    @Size(min = 10, max = 10, message = "Voter ID must be exactly 10 characters")
    @Column(unique = true)
    private String voterId;

    @Positive(message = "Age must be positive")
    private int age;

    private String occupation;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    public enum EmploymentStatus {
        EMPLOYED, UNEMPLOYED, RETIRED, STUDENT
    }

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

    private boolean active = true;

    private Boolean registeredOffline;

    private LocalDateTime syncTimestamp;

    @OneToMany(mappedBy = "voter")
    private List<Welfare> welfares;
}