package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RideRequestDto {
    
    @NotNull(message = "Pickup latitude is required")
    private Double pickupLatitude;
    
    @NotNull(message = "Pickup longitude is required")
    private Double pickupLongitude;
    
    private String pickupAddress;
    
    @NotNull(message = "Destination latitude is required")
    private Double destinationLatitude;
    
    @NotNull(message = "Destination longitude is required")
    private Double destinationLongitude;
    
    private String destinationAddress;
}
