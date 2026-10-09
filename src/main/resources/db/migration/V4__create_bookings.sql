CREATE TABLE bookings (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT        NOT NULL REFERENCES users(id),
    event_id        BIGINT        NOT NULL REFERENCES events(id),
    status          VARCHAR(20)   NOT NULL
                    CHECK (status IN ('HELD','CONFIRMED','EXPIRED','CANCELLED')),
    total_amount    NUMERIC(10,2) NOT NULL,
    hold_expires_at TIMESTAMPTZ   NOT NULL,
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT now()
);
CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_status_expiry ON bookings(status, hold_expires_at);

CREATE TABLE booking_items (
    id            BIGSERIAL PRIMARY KEY,
    booking_id    BIGINT        NOT NULL REFERENCES bookings(id),
    event_seat_id BIGINT        NOT NULL REFERENCES event_seats(id),
    price         NUMERIC(10,2) NOT NULL,
    active        BOOLEAN       NOT NULL DEFAULT TRUE
);
CREATE INDEX idx_booking_items_booking ON booking_items(booking_id);
CREATE INDEX idx_booking_items_seat ON booking_items(event_seat_id);