package com.example.Learn.LearnOne.Repository;

import com.example.Learn.LearnOne.Entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByStatus(Survey.SurveyStatus status);

    List<Survey> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT s FROM Survey s WHERE s.id = :surveyId")
    List<Survey> findBySurveyId(@Param("surveyId") Long surveyId);

    @Query("SELECT s FROM Survey s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Survey> searchSurveys(@Param("keyword") String keyword);
}