package ru.arman.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@Builder
public class PaymentDto {
    @DecimalMin(value = "0.0")
    private BigDecimal priceForDay;
    @NotBlank(message = "Card Number can't be empty")
    private String cardNumber;
    @NotBlank(message = "Card holder name can't be empty")
    private String cardHolder;
    @NotBlank(message = "Card date can't be empty")
    @Pattern(regexp = "^(0[1-9]|1[0-2])(\\/|-)([0-9]{2})$",
    message = "Card expiration date is not valid")
    private String dateValue;
    @Size(min = 3, max = 3)
    private String cvc;
    @NotNull(message = "Due date can't be null")
    private Date dueDate;
    @NotNull(message = "Return date can't be null")
    private Date returnDate;
    @NotNull(message = "Customer id can't be null")
    private Long customerId;
}
