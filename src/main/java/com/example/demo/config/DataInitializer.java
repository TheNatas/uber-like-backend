package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DataInitializer - Now handled by Flyway migrations
 * Initial data is loaded via V3__insert_initial_data.sql
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Data initialization is now handled by Flyway migrations
        // See: db/migration/V3__insert_initial_data.sql
        System.out.println("Application started. Database initialized with Flyway.");
    }
}
