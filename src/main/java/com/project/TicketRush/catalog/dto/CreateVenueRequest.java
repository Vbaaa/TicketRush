package com.project.TicketRush.catalog.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateVenueRequest(@NotBlank String name, @NotBlank String city, String address) {}

