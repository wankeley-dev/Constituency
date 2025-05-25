package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Survey;
import com.example.Learn.LearnOne.Entity.SurveyQuestion;
import com.example.Learn.LearnOne.Services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @GetMapping
    public String showSurveysPage(Model model,
                                  @RequestParam(required = false) String title,
                                  @RequestParam(required = false) Survey.SurveyStatus status,
                                  @RequestParam(required = false) String keyword) {
        try {
            List<Survey> surveys = surveyService.filterSurveys(title, status, keyword);
            model.addAttribute("surveys", surveys);
            model.addAttribute("title", title);
            model.addAttribute("status", status);
            model.addAttribute("keyword", keyword);
            model.addAttribute("survey", new Survey()); // For the create form
            model.addAttribute("question", new SurveyQuestion()); // For the add question form
            model.addAttribute("statuses", Survey.SurveyStatus.values());
            return "Survey/surveys"; // Adjust path if needed (e.g., "surveys" if in templates root)
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to load surveys: " + e.getMessage());
            return "error"; // Create an error.html page
        }
    }

    @PostMapping("/add")
    public String addSurvey(@ModelAttribute Survey survey) {
        try {
            surveyService.createSurvey(survey);
            return "redirect:/surveys";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/surveys?error=" + e.getMessage();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteSurvey(@PathVariable Long id) {
        try {
            surveyService.deleteSurvey(id);
            return "redirect:/surveys";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/surveys?error=" + e.getMessage();
        }
    }

    @PostMapping("/{id}/add-question")
    public String addQuestion(@PathVariable("id") Long id,
                              @ModelAttribute SurveyQuestion question,
                              @RequestParam("surveyId") Long surveyId) {
        try {
            if (!id.equals(surveyId)) {
                throw new IllegalArgumentException("Mismatch between path ID and survey ID.");
            }

            Survey survey = surveyService.getSurveyById(id)
                    .orElseThrow(() -> new RuntimeException("Survey not found with ID: " + id));

            question.setSurvey(survey);
            surveyService.addQuestionToSurvey(id, question);

            return "redirect:/surveys";
        } catch (IllegalArgumentException e) {
            return "redirect:/surveys?error=Invalid survey ID";
        } catch (RuntimeException e) {
            return "redirect:/surveys?error=" + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/surveys?error=An unexpected error occurred";
        }

}

    @GetMapping("/viewSurvey")
    public String viewSurveys(Model model) {
        model.addAttribute("surveys", surveyService.getAllSurveys());
        return "Survey/viewSurvey";
    }
}
