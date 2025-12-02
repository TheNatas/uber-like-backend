package com.example.demo.repository;

import com.example.demo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByIdAndPassenger_Id(Long id, Long passengerId);
    boolean existsByNumberAndPassenger_Id(String number, Long passengerId);

    @Query("SELECT p FROM Payment p WHERE p.status = 'ACTIVATED' AND p.passenger.id = :userId")
    List<Payment> findAllActivatedByUserId(Long userId);
}