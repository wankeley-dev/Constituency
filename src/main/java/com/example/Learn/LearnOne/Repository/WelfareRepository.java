package com.example.Learn.LearnOne.Repository;

import com.example.Learn.LearnOne.Entity.Event;
import com.example.Learn.LearnOne.Entity.Welfare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WelfareRepository extends JpaRepository<Welfare, Long> {
    boolean existsByVoter_VoterId(String voterId);
    Optional<Welfare> findByVoter_VoterId(String voterId);
    List<Welfare> findByWard(String ward);
    List<Welfare> findByPaymentStatus(String paymentStatus);
    List<Welfare> findByDueDateBeforeAndPaymentStatusNot(LocalDate date, String paymentStatus);
    List<Welfare> findByCategory(Welfare.BeneficiaryCategory category);

    @Query("SELECT w FROM Welfare w WHERE w.startDate BETWEEN :start AND :end")
    List<Welfare> findByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT w.category, COUNT(w), SUM(w.amountPaid) FROM Welfare w GROUP BY w.category")
    List<Object[]> getCategoryDistributionReport();

    @Query("SELECT w.ward, COUNT(w), SUM(w.amountPaid) FROM Welfare w WHERE w.startDate BETWEEN :start AND :end GROUP BY w.ward")
    List<Object[]> getWardDistributionReport(@Param("start") LocalDate start, @Param("end") LocalDate end);



}