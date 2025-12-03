package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;
    
    @NotBlank(message = "Make is required")
    @Column(nullable = false)
    private String make;
    
    @NotBlank(message = "Model is required")
    @Column(nullable = false)
    private String model;
    
    @Column(nullable = false, name = "vehicle_year")
    private Integer year;
    
    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^[A-Z0-9]{6,8}$", message = "License plate format is invalid")
    @Column(name = "license_plate", unique = true, nullable = false)
    private String licensePlate;
    
    @NotBlank(message = "Color is required")
    @Column(nullable = false)
    private String color;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type = VehicleType.STANDARD;
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum VehicleType {
        STANDARD, PREMIUM, SUV, ELECTRIC, SEDAN
    }
}
