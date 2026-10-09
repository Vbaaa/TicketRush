package com.project.TicketRush.booking;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id") private Long userId;
    @Column(name = "event_id") private Long eventId;
    @Enumerated(EnumType.STRING) private BookingStatus status = BookingStatus.HELD;
    @Column(name = "total_amount") private BigDecimal totalAmount;
    @Column(name = "hold_expires_at") private Instant holdExpiresAt;
}
