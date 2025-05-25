package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.Event;
import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Entity.VoterEventParticipation;
import com.example.Learn.LearnOne.Repository.VoterEventParticipationRepository;
import com.example.Learn.LearnOne.Repository.WelfareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class VoterEventParticipationService {

    @Autowired
    private VoterEventParticipationRepository participationRepository;
    @Autowired
    private WelfareRepository welfareRepository; // Added dependency

    public List<VoterEventParticipation> getAllParticipations() {
        return participationRepository.findAll();
    }

    public VoterEventParticipation registerVoterForEvent(Voter voter, Event event) {
        VoterEventParticipation participation = new VoterEventParticipation();
        participation.setVoter(voter);
        participation.setEvent(event);
        participation.setParticipationDate(event.getEventStartDateTime() != null ? event.getEventStartDateTime() : LocalDateTime.now());
        participation.setStatus(VoterEventParticipation.ParticipationStatus.REGISTERED);
        return participationRepository.save(participation);
    }

    public VoterEventParticipation updateParticipation(Long id, VoterEventParticipation updatedParticipation) {
        return participationRepository.findById(id).map(participation -> {
            participation.setStatus(updatedParticipation.getStatus());
            participation.setNotes(updatedParticipation.getNotes());
            return participationRepository.save(participation);
        }).orElseThrow(() -> new IllegalArgumentException("Participation not found"));
    }

    public void deleteParticipation(Long id) {
        participationRepository.deleteById(id);
    }

    public List<VoterEventParticipation> filterParticipations(Long voterId, Long eventId, VoterEventParticipation.ParticipationStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        if (voterId != null) {
            return participationRepository.findByVoterId(voterId);
        }
        if (eventId != null) {
            return participationRepository.findByEventId(eventId);
        }
        if (status != null) {
            return participationRepository.findByStatus(status);
        }
        if (startDate != null && endDate != null) {
            return participationRepository.findByDateRange(startDate, endDate);
        }
        return participationRepository.findAll();
    }

    public Map<String, Object> generateParticipationReport() {
        List<Object[]> results = participationRepository.getEventParticipationReport();
        Map<String, Object> report = new HashMap<>();
        List<Long> eventIds = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        for (Object[] result : results) {
            eventIds.add((Long) result[0]);
            counts.add((Long) result[1]);
        }

        report.put("eventIds", eventIds);
        report.put("counts", counts);
        return report;
    }

    public Optional<VoterEventParticipation> getParticipationById(Long id) {
        return participationRepository.findById(id);
    }

    // Completed method to count welfare beneficiaries by ward
    public Integer getWelfareBeneficiariesByWard(String ward) {
        return Math.toIntExact(welfareRepository.findByWard(ward).size());
    }

    // Optimized method to count event participation by ward
    public Integer getEventParticipationByWard(String ward) {
        return participationRepository.countByEventWard(ward);
    }
}