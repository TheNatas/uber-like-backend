package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DriverRegistrationDto {
    
    @NotBlank(message = "License number is required")
    private String licenseNumber;
    
    private String vehicleMake;
    
    private String vehicleModel;
    
    private Integer vehicleYear;
    
    private String vehicleLicensePlate;
    
    private String vehicleColor;
    
    private String vehicleType = "STANDARD";
}
