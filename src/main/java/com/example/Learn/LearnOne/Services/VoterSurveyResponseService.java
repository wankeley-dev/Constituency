package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.Survey;
import com.example.Learn.LearnOne.Entity.SurveyQuestion;
import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Entity.VoterSurveyResponse;
import com.example.Learn.LearnOne.Repository.VoterSurveyResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VoterSurveyResponseService {

    @Autowired
    private VoterSurveyResponseRepository responseRepository;

    public List<VoterSurveyResponse> getAllResponses() {
        return responseRepository.findAll();
    }

    public List<VoterSurveyResponse> getResponsesBySurvey(Long surveyId) {
        return responseRepository.findBySurveyId(surveyId);
    }

    public List<VoterSurveyResponse> getResponsesByVoter(Long voterId) {
        return responseRepository.findByVoterId(voterId);
    }

    public List<VoterSurveyResponse> findByQuestionId(Long questionId) {
        return responseRepository.findByQuestionId(questionId);
    }

    public VoterSurveyResponse responseSave(Voter voter, Survey survey, SurveyQuestion question,
                                            String responseText, Integer rating) {
        VoterSurveyResponse response = new VoterSurveyResponse();
        response.setVoter(voter);
        response.setSurvey(survey);
        response.setQuestion(question);
        response.setResponseText(responseText);
        response.setRating(rating);
        response.setSubmittedAt(LocalDateTime.now()); // Explicitly set submittedAt
        return responseRepository.save(response);
    }
}