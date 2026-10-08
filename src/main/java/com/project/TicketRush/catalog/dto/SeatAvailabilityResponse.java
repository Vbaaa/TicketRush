package com.project.TicketRush.catalog.dto;

import com.project.TicketRush.catalog.EventSeatStatus;

import java.math.BigDecimal;

public record SeatAvailabilityResponse(Long eventSeatId, String section, String row,
                                       int seatNumber, BigDecimal price, EventSeatStatus status) {}
