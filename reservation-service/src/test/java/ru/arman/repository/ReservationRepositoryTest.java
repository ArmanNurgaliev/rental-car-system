package ru.arman.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.arman.TestDataConfiguration;
import ru.arman.entity.Reservation;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(TestDataConfiguration.class)
@Sql(scripts={"classpath:reservations.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void findOverlappingReservationsTest_shouldReturn() {
        Date startDate = Date.valueOf("2024-05-10");
        Date endDate = Date.valueOf("2024-05-17");

        List<Reservation> reservations = reservationRepository.findOverlappingReservations(startDate, endDate);
        assertEquals(3, reservations.size());
    }

    @Test
    void findVehiclesChangedLocation() {
        Date returnDate = Date.valueOf("2024-05-21");

        Optional<Reservation> reservationOptional =
                reservationRepository.findVehiclesChangedLocation(2L, returnDate, 1L);

        assertTrue(reservationOptional.isPresent());
        assertEquals(2L, reservationOptional.get().getId());
    }

    @Test
    void findReservationByVehicleAndDates() {
        Date startDate = Date.valueOf("2024-05-20");
        Date endDate = Date.valueOf("2024-06-15");

        List<Reservation> reservations =
                reservationRepository.findReservationByVehicleAndDates(1L, startDate, endDate);

        assertEquals(2, reservations.size());
    }
}