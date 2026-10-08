package com.project.TicketRush.Repositories;

import com.project.TicketRush.catalog.Event;
import com.project.TicketRush.catalog.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByStatusAndStartTimeAfter(EventStatus status, Instant after, Pageable pageable);
}
