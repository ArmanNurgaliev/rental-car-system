package ru.arman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.arman.entity.Reservation;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.dueDate < ?2 AND r.returnDate > ?1")
    List<Reservation> findOverlappingReservations(Date startDate, Date endDate);

    @Query("SELECT r FROM Reservation r WHERE r.vehicleId = ?1 AND r.returnDate < ?2 ORDER BY r.returnDate DESC LIMIT 1")
    Optional<Reservation> findVehiclesChangedLocation(Long vehicleId, Date returnDate, Long returnLocationId);

    @Query("SELECT r FROM Reservation r WHERE r.vehicleId = ?1 AND (r.dueDate < ?3 AND r.returnDate > ?2)")
    List<Reservation> findReservationByVehicleAndDates(Long vehicleId, Date dueDate, Date returnDate);
}