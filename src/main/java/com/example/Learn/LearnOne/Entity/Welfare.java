package com.example.Learn.LearnOne.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

@Entity
public class Welfare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @JoinColumn(name = "voter_id", referencedColumnName = "voterId")
    @NotNull(message = "Voter is required")
    private Voter voter; // Link to Voter entity

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Voter getVoter() { return voter; }
    public void setVoter(Voter voter) { this.voter = voter; }

    // Convenience method to access voterId via Voter
    public String getVoterId() {
        return voter != null ? voter.getVoterId() : null;
    }
}