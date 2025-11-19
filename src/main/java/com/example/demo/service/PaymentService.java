package com.example.demo.service;

import com.example.demo.dto.CreditCardDto;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Payment;
import com.example.demo.entity.enums.PaymentStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
	
    public Payment createPayment(CreditCardDto creditCardDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean alreadyRegistered = paymentRepository.existsByNumberAndUser(creditCardDto.getNumber(), userId);
        if (alreadyRegistered)
            throw new BadRequestException("Credit card already registered");

        Payment payment = new Payment();
        payment.setDescription(creditCardDto.getDescription());
        payment.setNumber(creditCardDto.getNumber());
        payment.setCode(creditCardDto.getCode());
        payment.setStatus(PaymentStatus.ACTIVATED);
        YearMonth expiration = creditCardDto.getExpirationDate();
        payment.setExpireDate(LocalDateTime.of(expiration.getYear(), expiration.getMonth(), 1, 0, 0));
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setPassenger(user);

        return paymentRepository.save(payment);
    }
    
    public Payment cancelPayment(Long paymentId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Payment payment = paymentRepository.findByIdAndUser(paymentId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        payment.setStatus(PaymentStatus.CANCELLED);
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPaymentsByUser(Long userId) {
        return paymentRepository.findAllActivatedByUserId(userId);
    }
}
