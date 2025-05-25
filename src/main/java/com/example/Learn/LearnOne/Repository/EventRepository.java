package com.example.Learn.LearnOne.Repository;

import com.example.Learn.LearnOne.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventNameContainingIgnoreCase(String eventName);
    List<Event> findByLocationContainingIgnoreCase(String location);
    List<Event> findByEventStartDateTime(LocalDate eventStartDateTime);

    @Query("SELECT e FROM Event e WHERE LOWER(e.eventName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.location) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Event> searchEvents(@Param("keyword") String keyword);
}