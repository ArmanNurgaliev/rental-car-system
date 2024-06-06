package ru.arman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.arman.dto.PaymentDto;
import ru.arman.dto.PaymentResponseDto;
import ru.arman.entity.Payment;
import ru.arman.exception.PaymentNotFoundException;
import ru.arman.repository.PaymentRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponseDto createPayment(PaymentDto paymentDto) {
        Payment payment = convertToEntity(paymentDto);
        Payment savedPayment = paymentRepository.save(payment);

        return PaymentResponseDto.builder()
                .id(savedPayment.getId())
                .customerId(savedPayment.getCustomerId())
                .amount(savedPayment.getAmount())
                .build();
    }

    private Payment convertToEntity(PaymentDto paymentDto) {
        long diff = paymentDto.getReturnDate().getTime() - paymentDto.getDueDate().getTime();
        long numOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        BigDecimal rentPrice = paymentDto.getPriceForDay().multiply(BigDecimal.valueOf(numOfDays));

        return Payment.builder()
                .amount(rentPrice)
                .cardNumber(paymentDto.getCardNumber())
                .cardHolder(paymentDto.getCardHolder())
                .dateValue(paymentDto.getDateValue())
                .cvc(paymentDto.getCvc())
                .customerId(paymentDto.getCustomerId())
                .paymentDate(new Date(System.currentTimeMillis()))
                .build();
    }

    public String cancelPayment(Long paymentId) {
        paymentRepository.findById(paymentId)
                        .orElseThrow(() -> new PaymentNotFoundException("No payment with id: " + paymentId));
        // return money

        paymentRepository.deleteById(paymentId);

        return "Payment cancelled";
    }
}