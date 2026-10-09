package com.project.TicketRush.Services;

import com.project.TicketRush.Repositories.*;
import com.project.TicketRush.catalog.*;
import com.project.TicketRush.catalog.EventStatus;
import com.project.TicketRush.catalog.dto.*;
import com.project.TicketRush.common.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository events;
    private final VenueRepository venues;
    private final SeatRepository seats;
    private final EventSeatRepository eventSeats;

    public EventResponse create(CreateEventRequest r, Long organizerId) {
        venues.findById(r.venueId())
                .orElseThrow(() -> new NotFoundException("Venue " + r.venueId() + " not found"));
        Event e = new Event();
        e.setName(r.name()); e.setDescription(r.description());
        e.setVenueId(r.venueId()); e.setStartTime(r.startTime());
        e.setOrganizerId(organizerId);
        return EventResponse.from(events.save(e));
    }

    public PageResponse<EventResponse> listPublished(int page, int size) {
        var pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50),
                Sort.by("startTime"));
        return PageResponse.of(
                events.findByStatusAndStartTimeAfter(EventStatus.PUBLISHED, Instant.now(), pageable)
                        .map(EventResponse::from));
    }

    public EventResponse get(Long id) {
        Event e = events.findById(id).orElseThrow(() -> new NotFoundException("Event not found"));
        if (e.getStatus() != EventStatus.PUBLISHED) throw new NotFoundException("Event not found");
        return EventResponse.from(e);
    }

    public List<SeatAvailabilityResponse> seats(Long eventId) {
        get(eventId); // throws 404 if not visible
        return eventSeats.findAvailability(eventId);
    }

    public void publish(Long id, Long id1, boolean b) {
    }

    // publish() is added on Day 5
}
