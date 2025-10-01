package com.example.demo.controller;

import com.example.demo.dto.DriverRegistrationDto;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        Map<String, Object> response = authService.registerUser(registrationDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDto loginDto) {
        Map<String, Object> response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/driver/register/{userId}")
    public ResponseEntity<Map<String, Object>> registerDriver(
            @PathVariable Long userId,
            @Valid @RequestBody DriverRegistrationDto driverDto) {
        Map<String, Object> response = authService.registerDriver(userId, driverDto);
        return ResponseEntity.ok(response);
    }
}
