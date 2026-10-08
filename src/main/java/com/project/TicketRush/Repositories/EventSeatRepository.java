package com.project.TicketRush.Repositories;

import com.project.TicketRush.catalog.EventSeat;
import com.project.TicketRush.catalog.dto.SeatAvailabilityResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventSeatRepository extends JpaRepository<EventSeat, Long> {
    boolean existsByEventId(Long eventId);

    @Query("""
            select new com.project.TicketRush.catalog.dto.SeatAvailabilityResponse(
                es.id, s.section, s.rowLabel, s.seatNumber, es.price, es.status)
            from EventSeat es join Seat s on s.id = es.seatId
            where es.eventId = :eventId
            order by s.section, s.rowLabel, s.seatNumber
            """)
    List<SeatAvailabilityResponse> findAvailability(@Param("eventId") Long eventId);
}
