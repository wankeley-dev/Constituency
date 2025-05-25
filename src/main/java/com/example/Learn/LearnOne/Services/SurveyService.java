package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.Survey;
import com.example.Learn.LearnOne.Entity.SurveyQuestion;
import com.example.Learn.LearnOne.Repository.SurveyQuestionRepository;
import com.example.Learn.LearnOne.Repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyQuestionRepository questionRepository;

    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    public Optional<Survey> getSurveyById(Long id) {
        return surveyRepository.findById(id);
    }

    public Survey createSurvey(Survey survey) {
        if (survey.getStatus() == null) {
            survey.setStatus(Survey.SurveyStatus.DRAFT);
        }
        if (survey.getStartDate() == null) {
            survey.setStartDate(LocalDateTime.now());
        }
        return surveyRepository.save(survey);
    }

    public Survey updateSurvey(Survey survey) {
        if (!surveyRepository.existsById(survey.getId())) {
            throw new RuntimeException("Survey not found with ID: " + survey.getId());
        }
        return surveyRepository.save(survey);
    }

    public void deleteSurvey(Long id) {
        if (!surveyRepository.existsById(id)) {
            throw new RuntimeException("Survey not found with ID: " + id);
        }
        surveyRepository.deleteById(id);
    }

    public List<Survey> filterSurveys(String title, Survey.SurveyStatus status, String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return surveyRepository.searchSurveys(keyword.trim());
        }
        if (title != null && !title.trim().isEmpty()) {
            return surveyRepository.findByTitleContainingIgnoreCase(title.trim());
        }
        if (status != null) {
            return surveyRepository.findByStatus(status);
        }
        return surveyRepository.findAll();
    }

    public Survey addQuestionToSurvey(Long surveyId, SurveyQuestion question) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found with ID: " + surveyId));

        question.setId(null);
        question.setSurvey(survey);

        question = questionRepository.save(question);

        survey.getQuestions().add(question);
        surveyRepository.save(survey);

        return survey;
    }

    public Optional<SurveyQuestion> getSurveyQuestionById(Long questionId) {
        return questionRepository.findById(questionId);
    }

    public List<Survey> findSurveyById(Long surveyId) {
        return surveyRepository.findBySurveyId(surveyId);
    }
}