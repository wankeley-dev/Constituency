package com.example.Learn.LearnOne.Repository;

import com.example.Learn.LearnOne.Entity.Users;
import com.example.Learn.LearnOne.Entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoterRepository extends JpaRepository<Voter, Long> {
    boolean existsByVoterId(String voterId);
    Optional<Voter> findByVoterId(String voterId);
    List<Voter> findByBranch(String branch);
    List<Voter> findByPollingStation(String pollingStation);
    List<Voter> findByActive(boolean active);
    Optional<Voter> findByUser(Users user);
    Optional<Voter> findByFullName(String fullName); // New method to find by fullName

    @Query("SELECT v FROM Voter v WHERE LOWER(v.voterId) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(v.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(v.branch) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(v.pollingStation) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Voter> searchVoters(@Param("keyword") String keyword);
}