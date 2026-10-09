package com.project.TicketRush;

import com.project.TicketRush.Services.*;
import com.project.TicketRush.Services.VenueService;
import com.project.TicketRush.auth.*;
import com.project.TicketRush.catalog.PriceTier;
import com.project.TicketRush.catalog.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@Profile("dev") @RequiredArgsConstructor
class DevDataSeeder implements CommandLineRunner {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final VenueService venues;
    private final EventService events;

    @Override
    public void run(String... args) {
        if (users.count() > 0) return;                 // only seed an empty database

        createUser("admin@ticketrush.dev", "Admin User", Role.ADMIN);
        User org = createUser("organizer@ticketrush.dev", "Olivia Organizer", Role.ORGANIZER);
        createUser("user@ticketrush.dev", "Uma User", Role.USER);

        VenueResponse v = venues.create(new CreateVenueRequest("Pune Arena", "Pune", "Baner"));
        venues.addSeats(v.id(), new BulkSeatsRequest("A", 10, 20, PriceTier.STANDARD));  // 200 seats
        EventResponse e = events.create(new CreateEventRequest(
                "Indie Rock Night", "Live music", v.id(),
                Instant.now().plus(30, ChronoUnit.DAYS)), org.getId());
        events.publish(e.id(), org.getId(), false);
    }

    private User createUser(String email, String name, Role role) {
        User u = new User();
        u.setEmail(email); u.setFullName(name); u.setRole(role);
        u.setPasswordHash(encoder.encode("Passw0rd!"));  // dev-only password
        return users.save(u);
    }
}
