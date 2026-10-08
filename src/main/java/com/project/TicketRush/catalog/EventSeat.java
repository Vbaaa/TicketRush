package com.project.TicketRush.catalog;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.*;


import java.math.BigDecimal;
import java.time.Instant;


@Entity @Table(name = "event_seats")
@Getter @Setter @NoArgsConstructor
public class EventSeat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_id") private Long eventId;
    @Column(name = "seat_id") private Long seatId;
    private BigDecimal price;
    @Enumerated(EnumType.STRING) private EventSeatStatus status = EventSeatStatus.AVAILABLE;
    @Column(name = "held_until") private Instant heldUntil;
    @Version private Long version;
}
