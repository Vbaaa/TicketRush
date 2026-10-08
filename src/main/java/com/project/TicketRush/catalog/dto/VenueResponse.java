package com.project.TicketRush.catalog.dto;

import com.project.TicketRush.catalog.Venue;

public record VenueResponse(Long id, String name, String city, String address) {
    public static VenueResponse from(Venue v) {
        return new VenueResponse(v.getId(), v.getName(), v.getCity(), v.getAddress());
    }
}