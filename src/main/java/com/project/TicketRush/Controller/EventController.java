package com.project.TicketRush.Controller;

import com.project.TicketRush.Services.*;
import com.project.TicketRush.catalog.dto.CreateEventRequest;
import com.project.TicketRush.catalog.dto.EventResponse;
import com.project.TicketRush.catalog.dto.SeatAvailabilityResponse;
import com.project.TicketRush.common.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events") @RequiredArgsConstructor
public class EventController {
    private final EventService service;

    @GetMapping
    public PageResponse<EventResponse> list(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        return service.listPublished(page, size);
    }

    @GetMapping("/{id}")
    public EventResponse get(@PathVariable Long id) { return service.get(id); }

    @GetMapping("/{id}/seats")
    public List<SeatAvailabilityResponse> seats(@PathVariable Long id) { return service.seats(id); }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public ResponseEntity<EventResponse> create(@Valid @RequestBody CreateEventRequest r,
                                                @AuthenticationPrincipal Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r, userId));
    }
}
