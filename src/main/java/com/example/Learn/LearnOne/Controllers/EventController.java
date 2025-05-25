package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Entity.Event;
import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public String showEventsPage(Model model,
                                 @RequestParam(required = false) String name,
                                 @RequestParam(required = false) String location,
                                 @RequestParam(required = false) String eventDate,
                                 @RequestParam(required = false) String keyword) {
        LocalDate parsedDate = (eventDate != null && !eventDate.isEmpty()) ? LocalDate.parse(eventDate) : null;
        List<Event> events = eventService.filterEvents(name, location, parsedDate, keyword);
        model.addAttribute("event", new Event());
        model.addAttribute("events",events);
        model.addAttribute("name", name);
        model.addAttribute("location", location);
        model.addAttribute("eventDate", eventDate);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", Event.EventStatus.values());
        return "Events/events";
    }

    @PostMapping("/add")
    public String addEvent(@ModelAttribute Event event) {
        eventService.createEvent(event);
        return "redirect:/events";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/events";
    }

    @GetMapping("/viewEvent")
    public String viewEvents(Model model) {
        model.addAttribute("events", eventService.findAllEvents());
        return "Events/viewEvent";
    }
}
