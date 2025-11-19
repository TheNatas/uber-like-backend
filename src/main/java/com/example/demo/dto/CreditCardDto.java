package com.example.demo.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.YearMonth;

@Data
public class CreditCardDto {
    private String description;

    @NotBlank(message = "Credit card number is required")
    @Pattern(regexp = "^[0-9]{13,19}$", message = "Invalid credit card number.")
    private String number;

    @NotBlank(message = "Security code is required")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "Invalid security code.")
    private String code;

    @NotNull(message = "Expiration date is required")
    @Future(message = "Invalid date")
    private YearMonth expirationDate;
}
