-- ==========================================
-- V3 - Insert Initial Data
-- ==========================================

-- BCrypt hashes:
-- admin123     →  $2a$10$uP6oYlIvna8bIhWc0vDoYeV7WwVjCaZsJa5S2sHPy/Qn8nCt7CmU6
-- password123  →  $2a$10$8Y1Lw5GKu9gckC9RZxZP9O8XO2no0RvrRZtGJPD7WvyaManIeZDVq


-- ==========================================
-- Insert Admin user
-- ==========================================

INSERT INTO users (first_name, last_name, email, phone_number, password, role)
VALUES ('Admin', 'User', 'admin@uber.com', '+1234567890',
        '$2a$10$uP6oYlIvna8bIhWc0vDoYeV7WwVjCaZsJa5S2sHPy/Qn8nCt7CmU6',
        'ADMIN');


-- ==========================================
-- Insert Passenger users
-- ==========================================

INSERT INTO users (first_name, last_name, email, phone_number, password, role)
VALUES ('John', 'Doe', '+1234567891@email.com', '+1234567891',
        '$2a$10$8Y1Lw5GKu9gckC9RZxZP9O8XO2no0RvrRZtGJPD7WvyaManIeZDVq',
        'PASSENGER');

INSERT INTO users (first_name, last_name, email, phone_number, password, role)
VALUES ('Jane', 'Smith', 'jane.smith@email.com', '+1234567892',
        '$2a$10$8Y1Lw5GKu9gckC9RZxZP9O8XO2no0RvrRZtGJPD7WvyaManIeZDVq',
        'PASSENGER');


-- ==========================================
-- Insert Driver users
-- ==========================================

INSERT INTO users (first_name, last_name, email, phone_number, password, role)
VALUES ('Mike', 'Johnson', 'mike.johnson@email.com', '+1234567893',
        '$2a$10$8Y1Lw5GKu9gckC9RZxZP9O8XO2no0RvrRZtGJPD7WvyaManIeZDVq',
        'DRIVER');

INSERT INTO users (first_name, last_name, email, phone_number, password, role)
VALUES ('Sarah', 'Wilson', 'sarah.wilson@email.com', '+1234567894',
        '$2a$10$8Y1Lw5GKu9gckC9RZxZP9O8XO2no0RvrRZtGJPD7WvyaManIeZDVq',
        'DRIVER');


-- ==========================================
-- Insert Drivers (user_id 4 and 5)
-- ==========================================

INSERT INTO drivers (user_id, license_number, rating, total_rides, status, current_latitude, current_longitude)
VALUES (4, 'DL123456789', 4.80, 250, 'ONLINE', 40.7128, -74.0060);

INSERT INTO drivers (user_id, license_number, rating, total_rides, status, current_latitude, current_longitude)
VALUES (5, 'DL987654321', 4.60, 180, 'ONLINE', 40.7580, -73.9855);


-- ==========================================
-- Insert Vehicles (drivers IDs 1 and 2)
-- ==========================================

INSERT INTO vehicles (driver_id, make, model, vehicle_year, color, license_plate, type)
VALUES (1, 'Toyota', 'Camry', 2022, 'Silver', 'ABC1234', 'SEDAN');

INSERT INTO vehicles (driver_id, make, model, vehicle_year, color, license_plate, type)
VALUES (2, 'Honda', 'Accord', 2023, 'Black', 'XYZ9876', 'SEDAN');
