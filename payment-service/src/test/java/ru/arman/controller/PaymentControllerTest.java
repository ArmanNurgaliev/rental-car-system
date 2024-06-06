package ru.arman.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.arman.TestDataConfiguration;
import ru.arman.dto.PaymentDto;
import ru.arman.entity.Payment;
import ru.arman.repository.PaymentRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestDataConfiguration.class)
class PaymentControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PaymentRepository paymentRepository;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder()
                .paymentDate(new Date(System.currentTimeMillis()))
                .amount(BigDecimal.valueOf(10000))
                .cardNumber("4568745896587458")
                .cardHolder("ARMAN NURGALIEV")
                .dateValue("12/26")
                .cvc("682")
                .customerId(1L)
                .build();

        paymentRepository.save(payment);
    }

    @AfterEach
    void tearDown() {
        paymentRepository.deleteAll();
    }

    @Test
    void createPaymentTest_shouldReturnPaymentDto() throws Exception {
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
        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(post("/api/payment/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.amount").value(1050));
    }

    @Test
    void cancelPaymentTest_shouldDeletePayment() throws Exception {
        mvc.perform(post("/api/payment/cancel/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Payment cancelled"));
    }

    @Test
    void cancelPaymentTest_shouldThrowException() throws Exception {
        mvc.perform(post("/api/payment/cancel/" + 2L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No payment with id: " + 2L));
    }
}