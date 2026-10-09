package com.project.TicketRush.booking;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity @Table(name = "booking_items")
@Getter
@Setter
@NoArgsConstructor
public class BookingItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "booking_id") private Long bookingId;
    @Column(name = "event_seat_id") private Long eventSeatId;
    private BigDecimal price;
    private boolean active = true;
}
