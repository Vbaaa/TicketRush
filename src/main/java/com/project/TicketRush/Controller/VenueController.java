package com.project.TicketRush.Controller;
import com.project.TicketRush.Services.*;
import com.project.TicketRush.catalog.dto.*;
import com.project.TicketRush.common.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController @RequestMapping("/venues") @RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class VenueController {
    private final VenueService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public ResponseEntity<VenueResponse> create(@Valid @RequestBody CreateVenueRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PostMapping("/{id}/seats/bulk")
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public Map<String, Integer> addSeats(@PathVariable Long id,
                                         @Valid @RequestBody BulkSeatsRequest r) {
        return Map.of("seatsCreated", service.addSeats(id, r));
    }
}