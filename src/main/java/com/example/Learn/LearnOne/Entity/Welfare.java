package com.example.Learn.LearnOne.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Welfare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users user;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Ward is required")
    private String ward;

    @PositiveOrZero(message = "Amount paid must be zero or positive")
    private double amountPaid;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate dueDate;

    @NotBlank(message = "Payment status is required")
    private String paymentStatus = "Pending"; // e.g., Pending, Paid, Overdue

    private String notes;

    @ManyToOne
    @JoinColumn(name = "voter_id", referencedColumnName = "id")
    @NotNull(message = "Voter is required")
    private Voter voter;

    // Welfare Beneficiary Categorization
    @Enumerated(EnumType.STRING)
    private BeneficiaryCategory category;

    public enum BeneficiaryCategory {
        ELDERLY, DISABLED, UNEMPLOYED, OTHER
    }

    // Assistance History
    @OneToMany(mappedBy = "welfare", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WelfareAssistance> assistanceHistory;
}