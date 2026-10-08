package com.project.TicketRush.catalog;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Entity @Table(name = "events")
@Getter @Setter @NoArgsConstructor
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "text") private String description;
    @Column(name = "venue_id") private Long venueId;
    @Column(name = "organizer_id") private Long organizerId;
    @Column(name = "start_time") private Instant startTime;
    @Enumerated(EnumType.STRING) private EventStatus status = EventStatus.DRAFT;
}