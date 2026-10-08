package com.project.TicketRush.catalog;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.*;


@Entity
@Table(name = "seats")
@Getter @Setter
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "venue_id") private Long venueId;
    private String section;
    @Column(name = "row_label") private String rowLabel;
    @Column(name = "seat_number") private int seatNumber;
    @Enumerated(EnumType.STRING) @Column(name = "price_tier") private PriceTier priceTier;
}
