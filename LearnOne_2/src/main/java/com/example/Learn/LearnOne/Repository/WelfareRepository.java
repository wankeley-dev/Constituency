package com.example.Learn.LearnOne.Repository;

import com.example.Learn.LearnOne.Entity.Welfare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WelfareRepository extends JpaRepository<Welfare, Long> {
    boolean existsByVoter_VoterId(String voterId); // Updated to use Voter relationship
    Optional<Welfare> findByVoter_VoterId(String voterId); // Updated
    List<Welfare> findByWard(String ward);
    List<Welfare> findByPaymentStatus(String paymentStatus);
    List<Welfare> findByDueDateBeforeAndPaymentStatusNot(LocalDate date, String paymentStatus);
}