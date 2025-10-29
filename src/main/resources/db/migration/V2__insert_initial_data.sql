-- Insert Admin user
INSERT INTO users (first_name, last_name, email, phone_number, password, role)
VALUES ('Admin', 'User', 'admin@uber.com', '+1234567890', '$2a$10$XPTj7W8.J5Nb5fYQZZ3QYO5KmXkLqvXq5HqXK5xXZP5qXZqXZqXZq', 'ADMIN');

-- Insert Passenger users
INSERT INTO users (first_name, last_name, email, phone_number, password, role)
VALUES 
    ('John', 'Doe', 'john.doe@email.com', '+1234567891', '$2a$10$XPTj7W8.J5Nb5fYQZZ3QYO5KmXkLqvXq5HqXK5xXZP5qXZqXZqXZq', 'PASSENGER'),
    ('Jane', 'Smith', 'jane.smith@email.com', '+1234567892', '$2a$10$XPTj7W8.J5Nb5fYQZZ3QYO5KmXkLqvXq5HqXK5xXZP5qXZqXZqXZq', 'PASSENGER');

-- Insert Driver users
INSERT INTO users (first_name, last_name, email, phone_number, password, role)
VALUES 
    ('Mike', 'Johnson', 'mike.johnson@email.com', '+1234567893', '$2a$10$XPTj7W8.J5Nb5fYQZZ3QYO5KmXkLqvXq5HqXK5xXZP5qXZqXZqXZq', 'DRIVER'),
    ('Sarah', 'Wilson', 'sarah.wilson@email.com', '+1234567894', '$2a$10$XPTj7W8.J5Nb5fYQZZ3QYO5KmXkLqvXq5HqXK5xXZP5qXZqXZqXZq', 'DRIVER');

-- Insert Driver profiles (assuming user IDs 4 and 5 are the drivers)
INSERT INTO drivers (user_id, license_number, rating, total_rides, status, current_latitude, current_longitude)
VALUES 
    (4, 'DL123456789', 4.80, 250, 'ONLINE', 40.7128, -74.0060),
    (5, 'DL987654321', 4.60, 180, 'ONLINE', 40.7580, -73.9855);

-- Insert Vehicles for drivers (assuming driver IDs 1 and 2)
INSERT INTO vehicles (driver_id, make, model, vehicle_year, color, license_plate, type)
VALUES 
    (1, 'Toyota', 'Camry', 2022, 'Silver', 'ABC1234', 'SEDAN'),
    (2, 'Honda', 'Accord', 2023, 'Black', 'XYZ9876', 'SEDAN');
