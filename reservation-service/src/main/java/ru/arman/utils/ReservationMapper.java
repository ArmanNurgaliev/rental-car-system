package ru.arman.utils;

import org.springframework.stereotype.Component;
import ru.arman.dto.ReservationDto;
import ru.arman.entity.Reservation;


@Component
public class ReservationMapper {
    public Reservation mapDtoToReservation(ReservationDto reservationDto) {
        return Reservation.builder()
                .dueDate(reservationDto.getDueDate())
                .returnDate(reservationDto.getReturnDate())
                .customerId(reservationDto.getCustomerId())
                .vehicleId(reservationDto.getVehicleId())
                .pickUpLocationId(reservationDto.getPickUpLocationId())
                .returnLocationId(reservationDto.getReturnLocationId())
                .build();
    }
}
