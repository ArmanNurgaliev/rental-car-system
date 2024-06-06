package ru.arman.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.arman.dto.PaymentDto;
import ru.arman.dto.PaymentResponseDto;
import ru.arman.entity.Payment;
import ru.arman.exception.PaymentNotFoundException;
import ru.arman.repository.PaymentRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;

    @MockBean
    private PaymentRepository paymentRepository;

    @Test
    void createPaymentTest_shouldReturnPaymentDto() {
        PaymentDto paymentDto = PaymentDto.builder()
                .priceForDay(BigDecimal.valueOf(150))
                .cardNumber("4568745896587458")
                .cardHolder("ARMAN NURGALIEV")
                .dateValue("12/26")
                .cvc("682")
                .dueDate(Date.valueOf(LocalDate.now()))
                .returnDate(Date.valueOf(LocalDate.now().plusDays(7)))
                .customerId(1L)
                .build();

        Payment payment = Payment.builder()
                .id(1L)
                .paymentDate(new Date(System.currentTimeMillis()))
                .amount(BigDecimal.valueOf(1050))
                .cardHolder(paymentDto.getCardHolder())
                .cardNumber(paymentDto.getCardNumber())
                .cvc(paymentDto.getCvc())
                .dateValue(paymentDto.getDateValue())
                .customerId(paymentDto.getCustomerId())
                .build();

        when(paymentRepository.save(any())).thenReturn(payment);

        PaymentResponseDto responseDto = paymentService.createPayment(paymentDto);

        assertEquals(responseDto.getCustomerId(), paymentDto.getCustomerId());
    }

    @Test
    void cancelPaymentTest_shouldReturnString() {
        Payment payment = Payment.builder().build();
        when(paymentRepository.findById(any())).thenReturn(Optional.ofNullable(payment));

        String response = paymentService.cancelPayment(1L);

        assertEquals("Payment cancelled", response);
    }

    @Test
    void cancelPaymentTest_shouldThrowException() {
        PaymentNotFoundException exception =
                assertThrows(PaymentNotFoundException.class, () -> paymentService.cancelPayment(1L));

        assertEquals("No payment with id: " + 1, exception.getMessage());
    }
}