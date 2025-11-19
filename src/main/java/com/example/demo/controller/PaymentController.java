package com.example.demo.controller;

import com.example.demo.dto.CreditCardDto;
import com.example.demo.entity.Payment;
import com.example.demo.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<Payment> insertCreditCard(
            @PathVariable Long userId,
            @Valid @RequestBody CreditCardDto creditCardDto
    ) {
        Payment payment = paymentService.createPayment(creditCardDto, userId);
        return ResponseEntity.ok(payment);
    }

    @PatchMapping("/cancel/{userId}/{creditCardId}")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<Payment> cancelCreditCard(
            @PathVariable Long userId,
            @PathVariable Long creditCardId
    ) {
        Payment payment = paymentService.cancelPayment(creditCardId, userId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/get-all/{userId}")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<List<Payment>> cancelCreditCard(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getAllPaymentsByUser(userId));
    }
}
