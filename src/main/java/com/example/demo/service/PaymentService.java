package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Payment;
import com.example.demo.entity.Ride;
import com.example.demo.entity.enums.PaymentMethod;
import com.example.demo.entity.enums.PaymentStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	
    public Payment createPayment(Ride ride, PaymentMethod method) {
        Payment payment = new Payment();
        payment.setRide(ride);
        payment.setPassenger(ride.getPassenger());
        payment.setAmount(ride.getEstimatedFare());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setMethod(method);

        return paymentRepository.save(payment);
    }
    
    public Payment processPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        payment.setStatus(PaymentStatus.PROCESSING);

        boolean success = true;

        if (success) {
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setCompletedAt(LocalDateTime.now());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        return paymentRepository.save(payment);
    }
    
    public Payment cancelPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        payment.setStatus(PaymentStatus.CANCELLED);
        return paymentRepository.save(payment);
    }
}
