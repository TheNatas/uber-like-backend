package com.example.demo.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Flyway Java-based migration to insert initial data with properly hashed passwords.
 * This approach is better than SQL migrations for password hashing because:
 * - Uses the same BCryptPasswordEncoder as the application
 * - Generates fresh hashes dynamically
 * - Maintains consistency with authentication logic
 */
public class V2__insert_initial_data extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(
            new SingleConnectionDataSource(context.getConnection(), true)
        );
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // Hash passwords
        String adminPassword = passwordEncoder.encode("admin123");
        String userPassword = passwordEncoder.encode("password123");
        
        // Insert Admin user
        jdbcTemplate.update(
            "INSERT INTO users (first_name, last_name, email, phone_number, password, role) " +
            "VALUES (?, ?, ?, ?, ?, ?)",
            "Admin", "User", "admin@uber.com", "+1234567890", adminPassword, "ADMIN"
        );
        
        // Insert Passenger users
        jdbcTemplate.update(
            "INSERT INTO users (first_name, last_name, email, phone_number, password, role) " +
            "VALUES (?, ?, ?, ?, ?, ?)",
            "John", "Doe", "john.doe@email.com", "+1234567891", userPassword, "PASSENGER"
        );
        
        jdbcTemplate.update(
            "INSERT INTO users (first_name, last_name, email, phone_number, password, role) " +
            "VALUES (?, ?, ?, ?, ?, ?)",
            "Jane", "Smith", "jane.smith@email.com", "+1234567892", userPassword, "PASSENGER"
        );
        
        // Insert Driver users
        jdbcTemplate.update(
            "INSERT INTO users (first_name, last_name, email, phone_number, password, role) " +
            "VALUES (?, ?, ?, ?, ?, ?)",
            "Mike", "Johnson", "mike.johnson@email.com", "+1234567893", userPassword, "DRIVER"
        );
        
        jdbcTemplate.update(
            "INSERT INTO users (first_name, last_name, email, phone_number, password, role) " +
            "VALUES (?, ?, ?, ?, ?, ?)",
            "Sarah", "Wilson", "sarah.wilson@email.com", "+1234567894", userPassword, "DRIVER"
        );
        
        // Insert Driver profiles (user IDs 4 and 5 are drivers)
        jdbcTemplate.update(
            "INSERT INTO drivers (user_id, license_number, rating, total_rides, status, current_latitude, current_longitude) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)",
            4, "DL123456789", 4.80, 250, "ONLINE", 40.7128, -74.0060
        );
        
        jdbcTemplate.update(
            "INSERT INTO drivers (user_id, license_number, rating, total_rides, status, current_latitude, current_longitude) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)",
            5, "DL987654321", 4.60, 180, "ONLINE", 40.7580, -73.9855
        );
        
        // Insert Vehicles for drivers (driver IDs 1 and 2)
        jdbcTemplate.update(
            "INSERT INTO vehicles (driver_id, make, model, vehicle_year, color, license_plate, type) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)",
            1, "Toyota", "Camry", 2022, "Silver", "ABC1234", "SEDAN"
        );
        
        jdbcTemplate.update(
            "INSERT INTO vehicles (driver_id, make, model, vehicle_year, color, license_plate, type) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)",
            2, "Honda", "Accord", 2023, "Black", "XYZ9876", "SEDAN"
        );
    }
}
