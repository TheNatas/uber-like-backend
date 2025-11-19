ALTER TABLE users
ADD CONSTRAINT unique_phone_number UNIQUE (phone_number);

ALTER TABLE rides
ADD COLUMN payment_method VARCHAR(30),
ADD COLUMN payment_id BIGINT;

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    passenger_id BIGINT NOT NULL,
    description VARCHAR(500),
    number VARCHAR(100),
    code VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    expire_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_passenger FOREIGN KEY (passenger_id) REFERENCES users(id) ON DELETE CASCADE
);

ALTER TABLE rides
ADD CONSTRAINT fk_ride_payment FOREIGN KEY (payment_id) REFERENCES payments(id) ON DELETE SET NULL;


CREATE INDEX idx_rides_payment_method ON rides(payment_method);
CREATE INDEX idx_rides_payment_id ON rides(payment_id);
CREATE INDEX idx_payments_passenger_id ON payments(passenger_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_expire_date ON payments(expire_date);