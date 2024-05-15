package ru.arman.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@Builder
public class PaymentDto {
    private BigDecimal priceForDay;
    private String cardNumber;
    private String cardHolder;
    private String dateValue;
    private String cvc;
    private Date dueDate;
    private Date returnDate;

    private Long customerId;
}
