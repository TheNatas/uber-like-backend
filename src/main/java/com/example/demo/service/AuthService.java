package com.example.demo.service;

import com.example.demo.dto.DriverRegistrationDto;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.entity.Driver;
import com.example.demo.entity.User;
import com.example.demo.entity.Vehicle;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.DriverRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public Map<String, Object> registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        
        if (userRepository.existsByPhoneNumber(registrationDto.getPhoneNumber())) {
            throw new BadRequestException("Phone number already exists");
        }

        User user = new User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(User.Role.valueOf(registrationDto.getRole().toUpperCase()));

        user = userRepository.save(user);

        String token = jwtTokenUtil.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("user", getUserInfo(user));
        response.put("token", token);
        response.put("message", "User registered successfully");

        return response;
    }

    @Transactional
    public Map<String, Object> registerDriver(Long userId, DriverRegistrationDto driverDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (user.getRole() != User.Role.DRIVER) {
            throw new BadRequestException("User must have DRIVER role");
        }

        if (driverRepository.existsByLicenseNumber(driverDto.getLicenseNumber())) {
            throw new BadRequestException("License number already exists");
        }

        Driver driver = new Driver();
        driver.setUser(user);
        driver.setLicenseNumber(driverDto.getLicenseNumber());
        driver = driverRepository.save(driver);

        // Create vehicle if details provided
        if (driverDto.getVehicleMake() != null && driverDto.getVehicleModel() != null) {
            if (vehicleRepository.existsByLicensePlate(driverDto.getVehicleLicensePlate())) {
                throw new BadRequestException("Vehicle license plate already exists");
            }

            Vehicle vehicle = new Vehicle();
            vehicle.setDriver(driver);
            vehicle.setMake(driverDto.getVehicleMake());
            vehicle.setModel(driverDto.getVehicleModel());
            vehicle.setYear(driverDto.getVehicleYear());
            vehicle.setLicensePlate(driverDto.getVehicleLicensePlate());
            vehicle.setColor(driverDto.getVehicleColor());
            vehicle.setType(Vehicle.VehicleType.valueOf(driverDto.getVehicleType().toUpperCase()));
            vehicleRepository.save(vehicle);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("driver", driver);
        response.put("message", "Driver registered successfully");

        return response;
    }

    public Map<String, Object> login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("user", getUserInfo(user));
        response.put("token", token);
        response.put("message", "Login successful");

        return response;
    }

    private Map<String, Object> getUserInfo(User user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("firstName", user.getFirstName());
        userInfo.put("lastName", user.getLastName());
        userInfo.put("email", user.getEmail());
        userInfo.put("phoneNumber", user.getPhoneNumber());
        userInfo.put("role", user.getRole());
        return userInfo;
    }
}
