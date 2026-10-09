package com.project.TicketRush.Repositories;

import com.project.TicketRush.catalog.EventSeat;
import com.project.TicketRush.catalog.EventSeatStatus;
import com.project.TicketRush.catalog.dto.SeatAvailabilityResponse;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
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

    // Used by the pessimistic strategy: SELECT ... FOR UPDATE, sorted so every request locks in the same order
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select es from EventSeat es where es.id in (:ids) order by es.id")
    List<EventSeat> findAllForUpdate(@Param("ids") List<Long> ids);

    // Used ONLY by the naive strategy: a blind write with no status check and no version check
    @Modifying
    @Query("update EventSeat es set es.status = :status, es.heldUntil = :until where es.id in (:ids)")
    int blindHold(@Param("ids") List<Long> ids,
                  @Param("status") EventSeatStatus status,
                  @Param("until") Instant until);
}
