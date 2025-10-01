package com.example.demo.controller;

import com.example.demo.entity.Driver;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverRepository driverRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        return ResponseEntity.ok(driver);
    }

    @GetMapping("/online")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Driver>> getOnlineDrivers() {
        List<Driver> drivers = driverRepository.findAllOnlineDrivers();
        return ResponseEntity.ok(drivers);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<Driver> updateDriverStatus(
            @PathVariable Long id,
            @RequestParam Driver.Status status) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        driver.setStatus(status);
        driver = driverRepository.save(driver);
        return ResponseEntity.ok(driver);
    }

    @PutMapping("/{id}/location")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<Driver> updateDriverLocation(
            @PathVariable Long id,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        driver.setCurrentLatitude(latitude);
        driver.setCurrentLongitude(longitude);
        driver = driverRepository.save(driver);
        return ResponseEntity.ok(driver);
    }
}
