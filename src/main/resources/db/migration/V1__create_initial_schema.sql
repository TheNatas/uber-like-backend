-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name          VARCHAR(100) NOT NULL,
    last_name           VARCHAR(100) NOT NULL,
    email               VARCHAR(255) NOT NULL UNIQUE,
    phone_number        VARCHAR(20)  NOT NULL,
    password            VARCHAR(255) NOT NULL,
    role                VARCHAR(20)  NOT NULL,
    enabled             BOOLEAN      DEFAULT TRUE,
    created_at          TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- Create drivers table
CREATE TABLE IF NOT EXISTS drivers (
    id BIGSERIAL PRIMARY KEY,
    user_id             BIGINT       NOT NULL UNIQUE,
    license_number      VARCHAR(50)  NOT NULL UNIQUE,
    rating              DECIMAL(3, 2) DEFAULT 5.00,
    total_rides         INT           DEFAULT 0,
    status              VARCHAR(20)   NOT NULL DEFAULT 'OFFLINE',
    current_latitude    DOUBLE PRECISION,
    current_longitude   DOUBLE PRECISION,
    created_at          TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id BIGSERIAL PRIMARY KEY,
    driver_id           BIGINT       NOT NULL,
    make                VARCHAR(50)  NOT NULL,
    model               VARCHAR(50)  NOT NULL,
    vehicle_year        INT          NOT NULL,
    color               VARCHAR(30)  NOT NULL,
    license_plate       VARCHAR(20)  NOT NULL UNIQUE,
    type                VARCHAR(20)  NOT NULL,
    is_active           BOOLEAN      DEFAULT TRUE,
    created_at          TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE CASCADE
);

-- Create rides table
CREATE TABLE IF NOT EXISTS rides (
    id BIGSERIAL PRIMARY KEY,
    passenger_id                BIGINT           NOT NULL,
    driver_id                   BIGINT,
    pickup_latitude             DOUBLE PRECISION NOT NULL,
    pickup_longitude            DOUBLE PRECISION NOT NULL,
    pickup_address              VARCHAR(500),
    destination_latitude        DOUBLE PRECISION NOT NULL,
    destination_longitude       DOUBLE PRECISION NOT NULL,
    destination_address         VARCHAR(500),
    status                      VARCHAR(20)      NOT NULL DEFAULT 'REQUESTED',
    estimated_fare              DECIMAL(10, 2),
    actual_fare                 DECIMAL(10, 2),
    distance_km                 DOUBLE PRECISION,
    estimated_duration_minutes  INT,
    actual_duration_minutes     INT,
    requested_at                TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    accepted_at                 TIMESTAMP,
    started_at                  TIMESTAMP,
    completed_at                TIMESTAMP,
    cancelled_at                TIMESTAMP,
    cancellation_reason         VARCHAR(500),
    created_at                  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (passenger_id)  REFERENCES users(id)    ON DELETE CASCADE,
    FOREIGN KEY (driver_id)     REFERENCES drivers(id)  ON DELETE SET NULL
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_users_email          ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role           ON users(role);

CREATE INDEX IF NOT EXISTS idx_drivers_status       ON drivers(status);
CREATE INDEX IF NOT EXISTS idx_drivers_user_id      ON drivers(user_id);

CREATE INDEX IF NOT EXISTS idx_vehicles_driver_id   ON vehicles(driver_id);
CREATE INDEX IF NOT EXISTS idx_vehicles_license_plate ON vehicles(license_plate);

CREATE INDEX IF NOT EXISTS idx_rides_passenger_id   ON rides(passenger_id);
CREATE INDEX IF NOT EXISTS idx_rides_driver_id      ON rides(driver_id);
CREATE INDEX IF NOT EXISTS idx_rides_status         ON rides(status);
CREATE INDEX IF NOT EXISTS idx_rides_requested_at   ON rides(requested_at);
