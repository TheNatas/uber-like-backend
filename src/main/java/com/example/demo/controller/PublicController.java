package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Uber-like Backend");
        response.put("version", "1.0.0");
        return response;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Uber-like Backend Application");
        response.put("description", "A Spring Boot backend application for ride-sharing services");
        response.put("features", new String[]{
            "User Registration & Authentication",
            "JWT Security",
            "Ride Request & Management",
            "Driver Management",
            "Vehicle Management",
            "Location-based Services"
        });
        response.put("entities", new String[]{"User", "Driver", "Vehicle", "Ride"});
        return response;
    }
}
