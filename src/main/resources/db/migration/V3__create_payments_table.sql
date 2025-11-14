CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    ride_id BIGINT NOT NULL,
    passenger_id BIGINT NOT NULL,
    
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    method VARCHAR(30) NOT NULL,

    provider_transaction_id VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,

    CONSTRAINT fk_payment_ride
        FOREIGN KEY (ride_id) REFERENCES ride(id),

    CONSTRAINT fk_payment_passenger
        FOREIGN KEY (passenger_id) REFERENCES users(id)
);
