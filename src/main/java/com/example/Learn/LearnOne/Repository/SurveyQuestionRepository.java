package com.example.Learn.LearnOne.Repository;

import com.example.Learn.LearnOne.Entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
}
