package com.example.demo.controller;

import com.example.demo.dto.RideRequestDto;
import com.example.demo.entity.Driver;
import com.example.demo.entity.Ride;
import com.example.demo.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping("/request/{passengerId}")
    @PreAuthorize("hasRole('PASSENGER') or hasRole('ADMIN')")
    public ResponseEntity<Ride> requestRide(
            @PathVariable Long passengerId,
            @Valid @RequestBody RideRequestDto rideRequestDto) {
        Ride ride = rideService.requestRide(passengerId, rideRequestDto);
        return ResponseEntity.ok(ride);
    }

    @PutMapping("/{rideId}/accept/{driverId}")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<Ride> acceptRide(
            @PathVariable Long rideId,
            @PathVariable Long driverId) {
        Ride ride = rideService.acceptRide(driverId, rideId);
        return ResponseEntity.ok(ride);
    }

    @PutMapping("/{rideId}/status")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<Ride> updateRideStatus(
            @PathVariable Long rideId,
            @RequestParam Ride.Status status) {
        Ride ride = rideService.updateRideStatus(rideId, status);
        return ResponseEntity.ok(ride);
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    public ResponseEntity<List<Ride>> getAvailableRides() {
        List<Ride> rides = rideService.getAvailableRides();
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Ride>> getRideHistory(
            @PathVariable Long userId,
            @RequestParam String userType) {
        List<Ride> rides = rideService.getRideHistory(userId, userType);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/nearby-drivers")
    @PreAuthorize("hasRole('PASSENGER') or hasRole('ADMIN')")
    public ResponseEntity<List<Driver>> getNearbyDrivers(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radiusKm) {
        List<Driver> drivers = rideService.getNearbyDrivers(latitude, longitude, radiusKm);
        return ResponseEntity.ok(drivers);
    }
}
