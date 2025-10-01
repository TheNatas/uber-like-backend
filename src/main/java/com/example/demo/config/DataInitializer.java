package com.example.demo.config;

import com.example.demo.entity.Driver;
import com.example.demo.entity.User;
import com.example.demo.entity.Vehicle;
import com.example.demo.repository.DriverRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        // Create Admin user
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@uber.com");
        admin.setPhoneNumber("+1234567890");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        // Create Passenger users
        User passenger1 = new User();
        passenger1.setFirstName("John");
        passenger1.setLastName("Doe");
        passenger1.setEmail("john.doe@email.com");
        passenger1.setPhoneNumber("+1234567891");
        passenger1.setPassword(passwordEncoder.encode("password123"));
        passenger1.setRole(User.Role.PASSENGER);
        userRepository.save(passenger1);

        User passenger2 = new User();
        passenger2.setFirstName("Jane");
        passenger2.setLastName("Smith");
        passenger2.setEmail("jane.smith@email.com");
        passenger2.setPhoneNumber("+1234567892");
        passenger2.setPassword(passwordEncoder.encode("password123"));
        passenger2.setRole(User.Role.PASSENGER);
        userRepository.save(passenger2);

        // Create Driver users
        User driverUser1 = new User();
        driverUser1.setFirstName("Mike");
        driverUser1.setLastName("Johnson");
        driverUser1.setEmail("mike.johnson@email.com");
        driverUser1.setPhoneNumber("+1234567893");
        driverUser1.setPassword(passwordEncoder.encode("password123"));
        driverUser1.setRole(User.Role.DRIVER);
        userRepository.save(driverUser1);

        User driverUser2 = new User();
        driverUser2.setFirstName("Sarah");
        driverUser2.setLastName("Wilson");
        driverUser2.setEmail("sarah.wilson@email.com");
        driverUser2.setPhoneNumber("+1234567894");
        driverUser2.setPassword(passwordEncoder.encode("password123"));
        driverUser2.setRole(User.Role.DRIVER);
        userRepository.save(driverUser2);

        // Create Driver profiles
        Driver driver1 = new Driver();
        driver1.setUser(driverUser1);
        driver1.setLicenseNumber("DL123456789");
        driver1.setRating(BigDecimal.valueOf(4.8));
        driver1.setTotalRides(250);
        driver1.setStatus(Driver.Status.ONLINE);
        driver1.setCurrentLatitude(40.7128);
        driver1.setCurrentLongitude(-74.0060);
        driverRepository.save(driver1);

        Driver driver2 = new Driver();
        driver2.setUser(driverUser2);
        driver2.setLicenseNumber("DL987654321");
        driver2.setRating(BigDecimal.valueOf(4.6));
        driver2.setTotalRides(180);
        driver2.setStatus(Driver.Status.ONLINE);
        driver2.setCurrentLatitude(40.7580);
        driver2.setCurrentLongitude(-73.9855);
        driverRepository.save(driver2);

        // Create Vehicles
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setDriver(driver1);
        vehicle1.setMake("Toyota");
        vehicle1.setModel("Camry");
        vehicle1.setYear(2022);
        vehicle1.setLicensePlate("ABC123");
        vehicle1.setColor("Silver");
        vehicle1.setType(Vehicle.VehicleType.STANDARD);
        vehicleRepository.save(vehicle1);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setDriver(driver2);
        vehicle2.setMake("Honda");
        vehicle2.setModel("Accord");
        vehicle2.setYear(2021);
        vehicle2.setLicensePlate("XYZ789");
        vehicle2.setColor("Black");
        vehicle2.setType(Vehicle.VehicleType.PREMIUM);
        vehicleRepository.save(vehicle2);

        System.out.println("Sample data initialized successfully!");
        System.out.println("Admin: admin@uber.com / admin123");
        System.out.println("Passengers: john.doe@email.com / password123, jane.smith@email.com / password123");
        System.out.println("Drivers: mike.johnson@email.com / password123, sarah.wilson@email.com / password123");
    }
}
