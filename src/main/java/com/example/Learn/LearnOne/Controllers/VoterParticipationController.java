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
        model.addAttribute("participations", participations);
        model.addAttribute("voters", voterService.findAllVoters());
        model.addAttribute("events", eventService.findAllEvents());
        model.addAttribute("statuses", VoterEventParticipation.ParticipationStatus.values());
        model.addAttribute("voterId", voterId);
        model.addAttribute("eventId", eventId);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
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
            return "Voter/participationInput";
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
