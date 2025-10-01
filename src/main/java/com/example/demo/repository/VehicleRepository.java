package com.example.demo.repository;

import com.example.demo.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    List<Vehicle> findByDriverId(Long driverId);
    
    List<Vehicle> findByDriverIdAndIsActiveTrue(Long driverId);
    
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    
    boolean existsByLicensePlate(String licensePlate);
    
    @Query("SELECT v FROM Vehicle v WHERE v.isActive = true")
    List<Vehicle> findAllActiveVehicles();
}
