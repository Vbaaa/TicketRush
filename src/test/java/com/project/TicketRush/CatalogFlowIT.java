package com.project.TicketRush;


import com.jayway.jsonpath.JsonPath;
import com.project.TicketRush.auth.*;
import com.project.TicketRush.auth.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.*;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.*;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;


import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties =
        "app.jwt.secret=dGVzdC1zZWNyZXQtdGVzdC1zZWNyZXQtdGVzdC1zZWNyZXQ=")
@AutoConfigureMockMvc
@Testcontainers
class CatalogFlowIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer(DockerImageName.parse("postgres:16"));

    @Autowired
    MockMvc mvc;
    @Autowired
    UserRepository users;
    @Autowired
    PasswordEncoder encoder;

    // ---------- helpers ----------
    private String tokenFor(String email, Role role) throws Exception {
        if (users.findByEmail(email).isEmpty()) {
            User u = new User();
            u.setEmail(email); u.setFullName("Test " + role);
            u.setPasswordHash(encoder.encode("Passw0rd!")); u.setRole(role);
            users.save(u);
        }
        String body = mvc.perform(post("/auth/login").contentType(APPLICATION_JSON)
                        .content("""
                    {"email":"%s","password":"Passw0rd!"}""".formatted(email)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.accessToken");
    }

    private int createVenueWithSeats(String token, int rows, int perRow) throws Exception {
        String v = mvc.perform(post("/venues").header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content("""
                    {"name":"Test Arena","city":"Pune"}"""))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int venueId = JsonPath.read(v, "$.id");
        mvc.perform(post("/venues/" + venueId + "/seats/bulk")
                        .header("Authorization", "Bearer " + token).contentType(APPLICATION_JSON)
                        .content("""
                    {"section":"A","rows":%d,"seatsPerRow":%d,"priceTier":"STANDARD"}"""
                                .formatted(rows, perRow)))
                .andExpect(status().isOk());
        return venueId;
    }

    private int createEvent(String token, int venueId) throws Exception {
        String e = mvc.perform(post("/events").header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content("""
                    {"name":"Test Concert","venueId":%d,"startTime":"2031-01-01T18:00:00Z"}"""
                                .formatted(venueId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(e, "$.id");
    }

    // ---------- tests ----------
    @Test
    void duplicateRegistrationReturns409() throws Exception {
        String json = """
            {"email":"dup@test.com","password":"Passw0rd!","fullName":"Dup"}""";
        mvc.perform(post("/auth/register").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
        mvc.perform(post("/auth/register").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void wrongPasswordReturns401() throws Exception {
        tokenFor("wrongpw@test.com", Role.USER);
        mvc.perform(post("/auth/login").contentType(APPLICATION_JSON)
                        .content("""
                    {"email":"wrongpw@test.com","password":"nope-nope-nope"}"""))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void publicEventListNeedsNoToken() throws Exception {
        mvc.perform(get("/events")).andExpect(status().isOk());
    }

    @Test
    void normalUserCannotCreateEvent() throws Exception {
        String token = tokenFor("plainuser@test.com", Role.USER);
        mvc.perform(post("/events").header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content("""
                    {"name":"X","venueId":1,"startTime":"2031-01-01T18:00:00Z"}"""))
                .andExpect(status().isForbidden());
    }

    @Test
    void organizerFlow_publishGeneratesSeats_andIsIdempotent() throws Exception {
        String org = tokenFor("org1@test.com", Role.ORGANIZER);
        int venueId = createVenueWithSeats(org, 2, 5);          // 10 seats
        int eventId = createEvent(org, venueId);

        mvc.perform(get("/events/" + eventId)).andExpect(status().isNotFound()); // still DRAFT

        mvc.perform(post("/events/" + eventId + "/publish").header("Authorization", "Bearer " + org))
                .andExpect(status().isOk());
        mvc.perform(post("/events/" + eventId + "/publish").header("Authorization", "Bearer " + org))
                .andExpect(status().isOk());                          // publish twice

        mvc.perform(get("/events/" + eventId + "/seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));         // not 20
    }

    @Test
    void otherOrganizerCannotPublish() throws Exception {
        String owner = tokenFor("owner@test.com", Role.ORGANIZER);
        String other = tokenFor("other@test.com", Role.ORGANIZER);
        int eventId = createEvent(owner, createVenueWithSeats(owner, 1, 3));

        mvc.perform(post("/events/" + eventId + "/publish").header("Authorization", "Bearer " + other))
                .andExpect(status().isForbidden());
    }
}
