package ru.arman.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.arman.dto.VehicleDto;
import ru.arman.entity.Reservation;
import ru.arman.repository.ReservationRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @MockBean
    private ReservationRepository reservationRepository;

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
}