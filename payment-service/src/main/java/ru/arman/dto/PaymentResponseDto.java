package ru.arman.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PaymentResponseDto {
    private Long id;
    private Long customerId;
    private BigDecimal amount;
}
