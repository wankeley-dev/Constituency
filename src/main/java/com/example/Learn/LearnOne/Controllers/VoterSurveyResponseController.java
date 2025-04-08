package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.*;
import com.example.Learn.LearnOne.Services.SurveyService;
import com.example.Learn.LearnOne.Services.UserService;
import com.example.Learn.LearnOne.Services.VoterService;
import com.example.Learn.LearnOne.Services.VoterSurveyResponseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/survey-responses")
public class VoterSurveyResponseController {

    @Autowired
    private VoterSurveyResponseService responseService;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private VoterService voterService;

    @Autowired

    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(VoterSurveyResponseController.class);

    @GetMapping
    public String showResponsesPage(Model model,
                                    @RequestParam(required = false) Long surveyId,
                                    @RequestParam(required = false) Long voterId) {
        try {
            List<VoterSurveyResponse> responses = surveyId != null ? responseService.getResponsesBySurvey(surveyId)
                    : voterId != null ? responseService.getResponsesByVoter(voterId)
                    : responseService.getAllResponses();
            model.addAttribute("responses", responses);
            model.addAttribute("surveys", surveyService.getAllSurveys());
            model.addAttribute("surveyId", surveyId);
            model.addAttribute("voterId", voterId);
            return "Survey/survey-responses";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to load responses: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/submit/{surveyId}")
    public String showSubmitResponseForm(Model model,
                                         @PathVariable Long surveyId,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Survey survey = surveyService.getSurveyById(surveyId)
                    .orElseThrow(() -> new RuntimeException("Survey not found with ID: " + surveyId));

            Optional<Users> userOpt = userService.findByEmail(userDetails.getUsername());
            if (userOpt.isEmpty()) {
                throw new RuntimeException("Authenticated user not found.");
            }

            model.addAttribute("survey", survey);
            return "Survey/submit-survey-response";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to load survey: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/submit/{surveyId}")
    public String submitSurveyResponse(@PathVariable Long surveyId,
                                       @RequestParam Map<String, String> responses,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       RedirectAttributes redirectAttributes) {
        logger.info("POST /survey-responses/submit/{} called with responses: {}", surveyId, responses);
        try {
            Optional<Users> userOpt = userService.findByEmail(userDetails.getUsername());
            if (userOpt.isEmpty()) {
                logger.error("Authenticated user not found for email: {}", userDetails.getUsername());
                throw new RuntimeException("Authenticated user not found.");
            }
            Users user = userOpt.get();

            List<Voter> voters = user.getVoter();
            if (voters == null || voters.isEmpty()) {
                logger.error("No voters associated with user: {}", user.getEmail());
                throw new RuntimeException("No voter associated with the authenticated user.");
            }
            Voter voter = voters.get(0);

            Survey survey = surveyService.getSurveyById(surveyId)
                    .orElseThrow(() -> new RuntimeException("Survey not found with ID: " + surveyId));

            for (Map.Entry<String, String> entry : responses.entrySet()) {
                String questionIdStr = entry.getKey();
                String responseText = entry.getValue();
                logger.debug("Processing response - questionId: {}, response: {}", questionIdStr, responseText);

                if ("_csrf".equals(questionIdStr)) {
                    logger.debug("Skipping CSRF token: {}", responseText);
                    continue;
                }

                Long questionId = Long.parseLong(questionIdStr);
                SurveyQuestion question = surveyService.getSurveyQuestionById(questionId)
                        .orElseThrow(() -> new RuntimeException("Question not found with ID: " + questionId));
                responseService.responseSave(voter, survey, question, responseText,
                        responseText.matches("[1-5]") ? Integer.parseInt(responseText) : null);
            }

            redirectAttributes.addFlashAttribute("message", "Survey responses submitted successfully.");
            logger.info("Survey responses submitted successfully for surveyId: {}", surveyId);
            return "redirect:/survey-responses/submit/" + surveyId;

        } catch (Exception e) {
            logger.error("Error submitting survey responses for surveyId: {}", surveyId, e);
            redirectAttributes.addFlashAttribute("error", "Failed to submit survey responses: " + e.getMessage());
            return "redirect:/survey-responses/submit/" + surveyId;
        }
    }

    @GetMapping("/analytics/{surveyId}")
    public String showAnalytics(Model model, @PathVariable Long surveyId) {
        // Fetch the survey with its questions
        Optional<Survey> surveyOpt = surveyService.getSurveyById(surveyId);
        if (surveyOpt.isEmpty()) {
            logger.warn("Survey not found for ID: {}", surveyId);
            return "redirect:/survey-responses"; // Redirect if survey doesn't exist
        }
        Survey survey = surveyOpt.get(); // Unwrap the Optional

        // Fetch all responses for this survey
        List<VoterSurveyResponse> responses = responseService.getResponsesBySurvey(surveyId);

        // Add to model
        model.addAttribute("survey", survey); // Add the Survey object, not the Optional
        model.addAttribute("responses", responses);
        logger.info("Survey added to model: {}", survey);
        logger.info("Responses added to model: {}", responses);

        return "Survey/survey-analytics"; // Path to the template
    }

}
