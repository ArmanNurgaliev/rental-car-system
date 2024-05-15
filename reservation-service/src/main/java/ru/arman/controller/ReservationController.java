package ru.arman.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.arman.dto.ReservationDto;
import ru.arman.dto.VehicleDto;
import ru.arman.service.ReservationService;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("api/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<String> reserveACar(@RequestBody @Valid ReservationDto reservationDto) {
        return ResponseEntity.ok(reservationService.reserveCar(reservationDto));
    }

    @GetMapping("/overlapping")
    public ResponseEntity<List<Long>> getReservedVehicles(@RequestParam Date startDate,
                                          @RequestParam Date endDate) {
        return ResponseEntity.ok(reservationService.getReservedVehicles(startDate, endDate));
    }

    @PostMapping("/changed-location")
    public ResponseEntity<List<Long>> getChangedLocationVehicles(@RequestBody @Valid List<VehicleDto> vehicle) {
        return ResponseEntity.ok(reservationService.getChangedLocationVehicles(vehicle));
    }

    @PostMapping("/cancel/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }
}
