package com.example.Learn.LearnOne.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event name is required")
    private String eventName; // Changed from 'name' to 'eventName' for consistency with previous usage

    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Ward is required")
    private String ward; // Added ward to tie events to constituencies

    private LocalDateTime eventStartDateTime; // Changed to LocalDateTime for precision
    private LocalDateTime eventEndDateTime;   // Added end time for duration tracking

    @Enumerated(EnumType.STRING)
    private EventStatus status; // Added status to track event lifecycle

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoterEventParticipation> participants;

    public enum EventStatus {
        PLANNED, ONGOING, COMPLETED, CANCELLED
    }

    // Corrected getEventName method
    public String getEventName() {
        return eventName;
    }

    // Optional: Convenience method to get participant count
    public int getParticipantCount() {
        return participants != null ? participants.size() : 0;
    }
}