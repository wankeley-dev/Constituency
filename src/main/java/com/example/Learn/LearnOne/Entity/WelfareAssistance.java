package com.example.Learn.LearnOne.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class WelfareAssistance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "welfare_id")
    private Welfare welfare;

    private LocalDate assistanceDate;

    @PositiveOrZero(message = "Amount must be zero or positive")
    private double amount;

    private String description;
}