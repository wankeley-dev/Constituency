package com.example.Learn.LearnOne.Repository;

import com.example.Learn.LearnOne.Entity.VoterSurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoterSurveyResponseRepository extends JpaRepository<VoterSurveyResponse, Long> {
    List<VoterSurveyResponse> findBySurveyId(Long surveyId);
    List<VoterSurveyResponse> findByVoterId(Long voterId);
    List<VoterSurveyResponse> findByQuestionId(Long questionId);
}