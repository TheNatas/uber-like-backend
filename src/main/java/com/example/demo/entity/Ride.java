package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.entity.enums.PaymentMethod;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "rides")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ride {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private User passenger;
    
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;
    
    @NotNull(message = "Pickup latitude is required")
    @Column(name = "pickup_latitude", nullable = false)
    private Double pickupLatitude;
    
    @NotNull(message = "Pickup longitude is required")
    @Column(name = "pickup_longitude", nullable = false)
    private Double pickupLongitude;
    
    @Column(name = "pickup_address")
    private String pickupAddress;
    
    @NotNull(message = "Destination latitude is required")
    @Column(name = "destination_latitude", nullable = false)
    private Double destinationLatitude;
    
    @NotNull(message = "Destination longitude is required")
    @Column(name = "destination_longitude", nullable = false)
    private Double destinationLongitude;
    
    @Column(name = "destination_address")
    private String destinationAddress;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.REQUESTED;
    
    @Column(name = "estimated_fare")
    private BigDecimal estimatedFare;
    
    @Column(name = "actual_fare")
    private BigDecimal actualFare;
    
    @Column(name = "distance_km")
    private Double distanceKm;
    
    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;
    
    @Column(name = "actual_duration_minutes")
    private Integer actualDurationMinutes;
    
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;
    
    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;
    
    @Column(name = "cancellation_reason")
    private String cancellationReason;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = true)
    private Payment payment;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (requestedAt == null) {
            requestedAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum Status {
        REQUESTED, ACCEPTED, DRIVER_ARRIVED, IN_PROGRESS, COMPLETED, CANCELLED, PENDING_PAYMENT
    }
}
