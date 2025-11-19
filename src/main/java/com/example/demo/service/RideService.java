package com.example.demo.service;

import com.example.demo.dto.RideRequestDto;
import com.example.demo.entity.Driver;
import com.example.demo.entity.Payment;
import com.example.demo.entity.Ride;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.PaymentMethod;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DriverRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.RideRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public Ride requestRide(Long passengerId, RideRequestDto rideRequestDto) {
        User passenger = userRepository.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        // Check if passenger already has an active ride
        Optional<Ride> activeRide = rideRepository.findActiveRideByPassengerId(passengerId);
        if (activeRide.isPresent()) {
            throw new BadRequestException("You already have an active ride");
        }

        if (rideRequestDto.getPaymentMethod() == PaymentMethod.CREDIT_CARD && rideRequestDto.getPaymentId() == null)
            throw new BadRequestException("No credit card selected");

        Payment payment;
        if (rideRequestDto.getPaymentMethod() == PaymentMethod.CREDIT_CARD)
            payment = paymentRepository.findById(rideRequestDto.getPaymentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        else
            payment = null;

        Ride ride = new Ride();
        ride.setPassenger(passenger);
        ride.setPickupLatitude(rideRequestDto.getPickupLatitude());
        ride.setPickupLongitude(rideRequestDto.getPickupLongitude());
        ride.setPickupAddress(rideRequestDto.getPickupAddress());
        ride.setDestinationLatitude(rideRequestDto.getDestinationLatitude());
        ride.setDestinationLongitude(rideRequestDto.getDestinationLongitude());
        ride.setDestinationAddress(rideRequestDto.getDestinationAddress());
        ride.setPaymentMethod(rideRequestDto.getPaymentMethod());
        ride.setPayment(payment);
        ride.setStatus(Ride.Status.REQUESTED);

        // Calculate estimated fare and distance (simplified calculation)
        double distance = calculateDistance(
                rideRequestDto.getPickupLatitude(), rideRequestDto.getPickupLongitude(),
                rideRequestDto.getDestinationLatitude(), rideRequestDto.getDestinationLongitude()
        );
        ride.setDistanceKm(distance);
        ride.setEstimatedFare(BigDecimal.valueOf(distance * 2.5 + 5.0)); // Base fare + per km
        ride.setEstimatedDurationMinutes((int) (distance * 2)); // Rough estimate

        return rideRepository.save(ride);
    }

    @Transactional
    public Ride acceptRide(Long driverId, Long rideId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        // Check if driver already has an active ride
        Optional<Ride> activeRide = rideRepository.findActiveRideByDriverId(driverId);
        if (activeRide.isPresent()) {
            throw new BadRequestException("You already have an active ride");
        }

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));

        if (ride.getStatus() != Ride.Status.REQUESTED) {
            throw new BadRequestException("Ride is not available for acceptance");
        }

        ride.setDriver(driver);
        ride.setStatus(Ride.Status.ACCEPTED);
        ride.setAcceptedAt(LocalDateTime.now());

        // Update driver status
        driver.setStatus(Driver.Status.ON_RIDE);
        driverRepository.save(driver);

        return rideRepository.save(ride);
    }

    @Transactional
    public Ride updateRideStatus(Long rideId, Ride.Status status) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));

        switch (status) {
            case DRIVER_ARRIVED:
                if (ride.getStatus() != Ride.Status.ACCEPTED) {
                    throw new BadRequestException("Invalid status transition");
                }
                break;
            case IN_PROGRESS:
                if (ride.getStatus() != Ride.Status.DRIVER_ARRIVED) {
                    throw new BadRequestException("Invalid status transition");
                }
                ride.setStartedAt(LocalDateTime.now());
                break;
            case PENDING_PAYMENT:
                if (ride.getStatus() != Ride.Status.IN_PROGRESS) {
                    throw new BadRequestException("Invalid status transition");
                }
                ride.setCompletedAt(LocalDateTime.now());
                ride.setActualFare(ride.getEstimatedFare());
                status = CreditCardTransction(ride);
                break;
            case COMPLETED:
                if (ride.getStatus() != Ride.Status.PENDING_PAYMENT && ride.getStatus() != Ride.Status.IN_PROGRESS) {
                    throw new BadRequestException("Invalid status transition");
                }
                CompleteRide(ride);
                break;
            case CANCELLED:
                ride.setCancelledAt(LocalDateTime.now());
                if (ride.getDriver() != null) {
                    Driver driver = ride.getDriver();
                    driver.setStatus(Driver.Status.ONLINE);
                    driverRepository.save(driver);
                }
                break;
        }

        ride.setStatus(status);
        return rideRepository.save(ride);
    }

    public List<Ride> getRideHistory(Long userId, String userType) {
        if ("PASSENGER".equals(userType)) {
            return rideRepository.findRideHistoryByPassengerId(userId);
        } else if ("DRIVER".equals(userType)) {
            return rideRepository.findRideHistoryByDriverId(userId);
        }
        throw new BadRequestException("Invalid user type");
    }

    public List<Ride> getAvailableRides() {
        return rideRepository.findByStatus(Ride.Status.REQUESTED);
    }

    public List<Driver> getNearbyDrivers(Double latitude, Double longitude, Double radiusKm) {
        return driverRepository.findNearbyOnlineDrivers(latitude, longitude, radiusKm);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula for calculating distance between two points on Earth
        final int R = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c; // Distance in km
    }

    private Ride.Status CreditCardTransction(Ride ride) {
        if (ride.getPaymentMethod() != PaymentMethod.CREDIT_CARD)
            return Ride.Status.PENDING_PAYMENT;

        try {
            //External Bank API transaction integration here
            CompleteRide(ride);
            return Ride.Status.COMPLETED;
        }
        catch (Exception e) {
            return Ride.Status.PENDING_PAYMENT;
        }
    }

    private void CompleteRide(Ride ride) {
        if (ride.getDriver() != null) {
            Driver driver = ride.getDriver();
            driver.setStatus(Driver.Status.ONLINE);
            driver.setTotalRides(driver.getTotalRides() + 1);
            driverRepository.save(driver);
        }
    }
}
