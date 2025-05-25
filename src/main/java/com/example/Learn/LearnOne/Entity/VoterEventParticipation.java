package com.example.Learn.LearnOne.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class VoterEventParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "voter_id", nullable = false)
    private Voter voter;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private LocalDateTime participationDate;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;

    private String notes;

    public enum ParticipationStatus {
        REGISTERED, ATTENDED, ABSENT, CANCELLED
    }
}