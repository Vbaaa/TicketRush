package com.project.TicketRush.Repositories;

import com.project.TicketRush.booking.Booking;
import com.project.TicketRush.booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserIdOrderByIdDesc(Long userId);

    List<Booking> findByStatusAndHoldExpiresAtBefore(BookingStatus status, Instant before);

    /** Compare-and-set: only changes the status if it is still what we expect. Returns rows changed (0 or 1). */
    @Modifying
    @Query("update Booking b set b.status = :newStatus where b.id = :id and b.status = :expected")
    int transition(@Param("id") Long id,
                   @Param("expected") BookingStatus expected,
                   @Param("newStatus") BookingStatus newStatus);
}

