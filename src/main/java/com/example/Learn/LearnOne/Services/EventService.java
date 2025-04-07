package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.Event;
import com.example.Learn.LearnOne.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public List<Event> filterEvents(String name, String location, LocalDate eventDate, String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return eventRepository.searchEvents(keyword);
        }
        if (name != null && !name.isEmpty()) {
            return eventRepository.findByEventNameContainingIgnoreCase(name);
        }
        if (location != null && !location.isEmpty()) {
            return eventRepository.findByLocationContainingIgnoreCase(location);
        }
        if (eventDate != null) {
            return eventRepository.findByEventStartDateTime(eventDate);
        }
        return eventRepository.findAll();
    }

    public Object findAllEvents() {
        return eventRepository.findAll();
    }
}