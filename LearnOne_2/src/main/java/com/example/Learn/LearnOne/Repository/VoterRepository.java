package com.example.Learn.LearnOne.Repository;

import com.example.Learn.LearnOne.Entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {
    boolean existsByVoterId(String voterId);
    Optional<Voter> findByVoterId(String voterId);
    List<Voter> findByBranch(String branch);
    List<Voter> findByPollingStation(String pollingStation);
    List<Voter> findByActive(boolean active);
    List<Voter> findByFullNameContainingIgnoreCase(String fullName);
}