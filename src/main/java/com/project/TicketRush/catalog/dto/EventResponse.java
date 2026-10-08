package com.project.TicketRush.catalog.dto;

import com.project.TicketRush.catalog.Event;

import java.time.Instant;

public record EventResponse(Long id, String name, String description,
                            Long venueId, Instant startTime, String status) {
    public static EventResponse from(Event e) {
        return new EventResponse(e.getId(), e.getName(), e.getDescription(),
                e.getVenueId(), e.getStartTime(), e.getStatus().name());
    }
}
