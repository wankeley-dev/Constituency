package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Event;
import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Entity.VoterEventParticipation;
import com.example.Learn.LearnOne.Services.EventService;
import com.example.Learn.LearnOne.Services.VoterEventParticipationService;
import com.example.Learn.LearnOne.Services.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/participation")
public class VoterParticipationController {

    @Autowired
    private VoterEventParticipationService participationService;

    @Autowired
    private VoterService voterService;

    @Autowired
    private EventService eventService;

    @GetMapping
    public String showParticipationForm(Model model) {
        model.addAttribute("participation", new VoterEventParticipation());
        model.addAttribute("voters", voterService.findAllVoters());
        model.addAttribute("events", eventService.findAllEvents());
        model.addAttribute("statuses", VoterEventParticipation.ParticipationStatus.values());
        return "Events/participationInput";
    }

    @PostMapping("/save")
    public String registerVoterForEvent(@ModelAttribute("participation") VoterEventParticipation participation,
                                        RedirectAttributes redirectAttributes) {
        Voter voter = voterService.getVoterById(participation.getVoter().getId())
                .orElseThrow(() -> new IllegalArgumentException("Voter not found"));
        Event event = eventService.getEventById(participation.getEvent().getId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        participation.setVoter(voter);
        participation.setEvent(event);
        participationService.registerVoterForEvent(voter, event);
        redirectAttributes.addFlashAttribute("message", "Voter registered for event successfully!");
        return "redirect:/participation/view";
    }

    @GetMapping("/view")
    public String viewParticipations(Model model,
                                     @RequestParam(required = false) Long voterId,
                                     @RequestParam(required = false) Long eventId,
                                     @RequestParam(required = false) VoterEventParticipation.ParticipationStatus status,
                                     @RequestParam(required = false) String startDate,
                                     @RequestParam(required = false) String endDate) {
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : null;
        List<VoterEventParticipation> participations = participationService.filterParticipations(voterId, eventId, status, start, end);

        // Initialize wardDistribution and eventParticipationByWard
        Map<String, Integer> wardDistribution = new HashMap<>();
        Map<String, Integer> eventParticipationByWard = new HashMap<>();

        // Populate wardDistribution (e.g., welfare beneficiaries per ward)
        // This is a placeholder; replace with actual logic from your service
        wardDistribution.put("Ward1", participationService.getWelfareBeneficiariesByWard("Ward1"));
        wardDistribution.put("Ward2", participationService.getWelfareBeneficiariesByWard("Ward2"));
        wardDistribution.put("Ward3", participationService.getWelfareBeneficiariesByWard("Ward3"));
        wardDistribution.put("Ward4", participationService.getWelfareBeneficiariesByWard("Ward4"));

        // Populate eventParticipationByWard
        eventParticipationByWard.put("Ward1", participationService.getEventParticipationByWard("Ward1"));
        eventParticipationByWard.put("Ward2", participationService.getEventParticipationByWard("Ward2"));
        eventParticipationByWard.put("Ward3", participationService.getEventParticipationByWard("Ward3"));
        eventParticipationByWard.put("Ward4", participationService.getEventParticipationByWard("Ward4"));

        model.addAttribute("participations", participations);
        model.addAttribute("voters", voterService.findAllVoters());
        model.addAttribute("events", eventService.findAllEvents());
        model.addAttribute("statuses", VoterEventParticipation.ParticipationStatus.values());
        model.addAttribute("voterId", voterId);
        model.addAttribute("eventId", eventId);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("wardDistribution", wardDistribution);
        model.addAttribute("eventParticipationByWard", eventParticipationByWard);

        return "Events/participationView";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<VoterEventParticipation> participation = participationService.getParticipationById(id);
        if (participation.isPresent()) {
            model.addAttribute("participation", participation.get());
            model.addAttribute("voters", voterService.findAllVoters());
            model.addAttribute("events", eventService.findAllEvents());
            model.addAttribute("statuses", VoterEventParticipation.ParticipationStatus.values());
            return "Events/participationInput"; // Fixed template path
        } else {
            redirectAttributes.addFlashAttribute("message", "Participation record not found!");
            return "redirect:/participation/view";
        }
    }

    @PostMapping("/update")
    public String updateParticipation(@ModelAttribute("participation") VoterEventParticipation participation,
                                      RedirectAttributes redirectAttributes) {
        participationService.updateParticipation(participation.getId(), participation);
        redirectAttributes.addFlashAttribute("message", "Participation updated successfully!");
        return "redirect:/participation/view";
    }

    @GetMapping("/delete/{id}")
    public String deleteParticipation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        participationService.deleteParticipation(id);
        redirectAttributes.addFlashAttribute("message", "Participation deleted successfully!");
        return "redirect:/participation/view";
    }

    @GetMapping("/report")
    public String generateReport(Model model) {
        Map<String, Object> report = participationService.generateParticipationReport();
        model.addAttribute("report", report);
        model.addAttribute("events", eventService.findAllEvents());
        return "Events/participationReport";
    }
}