package com.project.TicketRush.Services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.project.TicketRush.Repositories.*;
import com.project.TicketRush.catalog.*;
import com.project.TicketRush.catalog.EventStatus;
import com.project.TicketRush.catalog.dto.*;
import com.project.TicketRush.common.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venues;
    private final SeatRepository seats;

    public VenueResponse create(CreateVenueRequest r) {
        Venue v = new Venue();
        v.setName(r.name()); v.setCity(r.city()); v.setAddress(r.address());
        return VenueResponse.from(venues.save(v));
    }

    /** Generates rows A, B, C... each with seats 1..seatsPerRow. Returns how many were created. */
    @Transactional
    public int addSeats(Long venueId, BulkSeatsRequest r) {
        venues.findById(venueId)
                .orElseThrow(() -> new NotFoundException("Venue " + venueId + " not found"));
        List<Seat> batch = new ArrayList<>();
        for (int row = 0; row < r.rows(); row++) {
            String label = String.valueOf((char) ('A' + row));
            for (int n = 1; n <= r.seatsPerRow(); n++) {
                Seat s = new Seat();
                s.setVenueId(venueId);
                s.setSection(r.section());
                s.setRowLabel(label);
                s.setSeatNumber(n);
                s.setPriceTier(r.priceTier());
                batch.add(s);
            }
        }
        seats.saveAll(batch);
        return batch.size();
    }
}
