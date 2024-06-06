package ru.arman.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.internal.duplex.DuplexResponseBody;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import ru.arman.dto.PaymentResponseDto;
import ru.arman.dto.ReservationDto;
import ru.arman.dto.VehicleDto;
import ru.arman.entity.Reservation;
import ru.arman.repository.ReservationRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @MockBean
    private ReservationRepository reservationRepository;

    private static MockWebServer mockWebServer;

    @MockBean
    private WebClient webclient;

    private Reservation reservation1;
    private Reservation reservation2;

    @BeforeEach
    void setUp() {
        reservation1 = Reservation.builder()
                .id(1L)
                .dueDate(Date.valueOf("2024-05-10"))
                .returnDate(Date.valueOf("2024-05-17"))
                .customerId(1L)
                .vehicleId(1L)
                .pickUpLocationId(1L)
                .returnLocationId(1L)
                .build();

        reservation2 = Reservation.builder()
                .id(2L)
                .dueDate(Date.valueOf("2024-05-15"))
                .returnDate(Date.valueOf("2024-05-25"))
                .customerId(2L)
                .vehicleId(2L)
                .pickUpLocationId(1L)
                .returnLocationId(2L)
                .build();
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(9090);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void reserveCarTest_shouldReturnString() throws JsonProcessingException {
        ReservationDto reservationDto = ReservationDto.builder()
                .priceForDay(BigDecimal.valueOf(50.00))
                .dueDate(Date.valueOf(LocalDate.now().plusDays(1)))
                .returnDate(Date.valueOf(LocalDate.now().plusDays(8)))
                .pickUpLocationId(1L)
                .returnLocationId(2L)
                .customerId(1L)
                .customerEmail("email.mail.ru")
                .vehicleId(5L)
                .cardNumber("5984526847851535")
                .cardHolder("CARD HOLDER")
                .dateValue("12/31")
                .cvc("135")
                .build();

        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .id(1L)
                .customerId(1L)
                .amount(BigDecimal.valueOf(350.00))
                .build();

        mockWebServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(paymentResponseDto))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );


        String answer = reservationService.reserveCar(reservationDto);

        assertEquals("You reserved a car. It will wait you: " + reservationDto.getDueDate(), answer);
    }

    @Test
    void getReservedVehiclesTest_shouldReturnList() {
        Date startDate = Date.valueOf("2024-05-10");
        Date endDate = Date.valueOf("2024-05-17");

        when(reservationRepository.findOverlappingReservations(any(), any())).thenReturn(List.of(reservation1, reservation2));

        List<Long> reservedVehicles = reservationService.getReservedVehicles(startDate, endDate);

        assertEquals(2, reservedVehicles.size());
    }

    @Test
    void getChangedLocationVehiclesTest_shouldReturnVehicleId() {
        VehicleDto vehicleDto = VehicleDto.builder()
                .locationId(1L)
                .vehicleId(2L)
                .startDate(Date.valueOf("2024-05-26"))
                .build();

        when(reservationRepository.findVehiclesChangedLocation(any(), any(), any())).thenReturn(Optional.of(reservation2));

        List<Long> changedLocationVehicles = reservationService.getChangedLocationVehicles(List.of(vehicleDto));

        assertEquals(1, changedLocationVehicles.size());
        assertEquals(reservation2.getVehicleId(), changedLocationVehicles.get(0));
    }

    @Test
    void cancelReservationTest_shouldReturnString() {
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation1));

        String answer = reservationService.cancelReservation(1L);

        assertEquals("Reservation cancelled.", answer);
    }
}