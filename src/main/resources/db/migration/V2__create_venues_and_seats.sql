CREATE TABLE venues (
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(160) NOT NULL,
    city    VARCHAR(80)  NOT NULL,
    address VARCHAR(255)
);

CREATE TABLE seats (
    id         BIGSERIAL PRIMARY KEY,
    venue_id   BIGINT NOT NULL REFERENCES venues(id),
    section    VARCHAR(20) NOT NULL,
    row_label  VARCHAR(10) NOT NULL,
    seat_number INT NOT NULL,
    price_tier VARCHAR(20) NOT NULL,
    UNIQUE (venue_id, section, row_label, seat_number)
);