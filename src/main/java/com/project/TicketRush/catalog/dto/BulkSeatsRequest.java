package com.project.TicketRush.catalog.dto;


import com.project.TicketRush.catalog.PriceTier;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BulkSeatsRequest(
        @NotBlank String section,
        @Min(1) @Max(26) int rows,
        @Min(1) @Max(100) int seatsPerRow,
        @NotNull PriceTier priceTier) {}
