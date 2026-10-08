package com.project.TicketRush.catalog.dto;

import jakarta.validation.constraints.*;

import java.time.Instant;

public record CreateEventRequest(
        @NotBlank @Size(max = 200) String name,
        String description,
        @NotNull Long venueId,
        @NotNull @Future Instant startTime) {}