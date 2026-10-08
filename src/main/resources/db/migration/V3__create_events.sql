CREATE TABLE events (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(200) NOT NULL,
    description  TEXT,
    venue_id     BIGINT NOT NULL REFERENCES venues(id),
    organizer_id BIGINT NOT NULL REFERENCES users(id),
    start_time   TIMESTAMPTZ NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'DRAFT'
                 CHECK (status IN ('DRAFT','PUBLISHED','CANCELLED')),
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_events_start_time ON events(start_time);
CREATE INDEX idx_events_venue ON events(venue_id);

CREATE TABLE event_seats (
    id         BIGSERIAL PRIMARY KEY,
    event_id   BIGINT NOT NULL REFERENCES events(id),
    seat_id    BIGINT NOT NULL REFERENCES seats(id),
    price      NUMERIC(10,2) NOT NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
               CHECK (status IN ('AVAILABLE','HELD','BOOKED')),
    held_until TIMESTAMPTZ,
    version    BIGINT NOT NULL DEFAULT 0,       -- for optimistic locking (week 2)
    UNIQUE (event_id, seat_id)
);
CREATE INDEX idx_event_seats_event_status ON event_seats(event_id, status);