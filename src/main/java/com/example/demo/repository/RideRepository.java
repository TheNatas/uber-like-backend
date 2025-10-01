package com.example.demo.repository;

import com.example.demo.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    
    List<Ride> findByPassengerId(Long passengerId);
    
    List<Ride> findByDriverId(Long driverId);
    
    List<Ride> findByStatus(Ride.Status status);
    
    @Query("SELECT r FROM Ride r WHERE r.passenger.id = :passengerId AND r.status IN ('REQUESTED', 'ACCEPTED', 'DRIVER_ARRIVED', 'IN_PROGRESS')")
    Optional<Ride> findActiveRideByPassengerId(@Param("passengerId") Long passengerId);
    
    @Query("SELECT r FROM Ride r WHERE r.driver.id = :driverId AND r.status IN ('ACCEPTED', 'DRIVER_ARRIVED', 'IN_PROGRESS')")
    Optional<Ride> findActiveRideByDriverId(@Param("driverId") Long driverId);
    
    @Query("SELECT r FROM Ride r WHERE r.passenger.id = :passengerId ORDER BY r.createdAt DESC")
    List<Ride> findRideHistoryByPassengerId(@Param("passengerId") Long passengerId);
    
    @Query("SELECT r FROM Ride r WHERE r.driver.id = :driverId ORDER BY r.createdAt DESC")
    List<Ride> findRideHistoryByDriverId(@Param("driverId") Long driverId);
    
    @Query("SELECT r FROM Ride r WHERE r.createdAt BETWEEN :startDate AND :endDate")
    List<Ride> findRidesBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
}
