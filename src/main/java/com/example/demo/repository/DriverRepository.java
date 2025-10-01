package com.example.demo.repository;

import com.example.demo.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    
    Optional<Driver> findByLicenseNumber(String licenseNumber);
    
    Optional<Driver> findByUserId(Long userId);
    
    @Query("SELECT d FROM Driver d WHERE d.status = 'ONLINE'")
    List<Driver> findAllOnlineDrivers();
    
    @Query("SELECT d FROM Driver d WHERE d.status = 'ONLINE' " +
           "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(d.currentLatitude)) * " +
           "cos(radians(d.currentLongitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(d.currentLatitude)))) <= :radiusKm")
    List<Driver> findNearbyOnlineDrivers(@Param("latitude") Double latitude, 
                                        @Param("longitude") Double longitude, 
                                        @Param("radiusKm") Double radiusKm);
    
    boolean existsByLicenseNumber(String licenseNumber);
}
