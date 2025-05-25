package com.example.Learn.LearnOne.Repository;

import com.example.Learn.LearnOne.Entity.VoterEventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VoterEventParticipationRepository extends JpaRepository<VoterEventParticipation, Long> {
    List<VoterEventParticipation> findByVoterId(Long voterId);
    List<VoterEventParticipation> findByEventId(Long eventId);
    List<VoterEventParticipation> findByStatus(VoterEventParticipation.ParticipationStatus status);

    @Query("SELECT p FROM VoterEventParticipation p WHERE p.participationDate BETWEEN :start AND :end")
    List<VoterEventParticipation> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT p.event.id, COUNT(p) FROM VoterEventParticipation p GROUP BY p.event.id")
    List<Object[]> getEventParticipationReport();

    // New query to count participations by event ward
    @Query("SELECT COUNT(p) FROM VoterEventParticipation p WHERE p.event.ward = :ward")
    Integer countByEventWard(@Param("ward") String ward);
}