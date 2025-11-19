package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.enums.PaymentMethod;
import com.example.demo.entity.enums.PaymentStatus;


@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ride> ride;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User passenger;

    @Column(name = "description")
    private String description;

    @Column(name = "number")
    private String number;

    @Column(name = "code")
    private String code;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
