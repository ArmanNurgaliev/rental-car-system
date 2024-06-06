package ru.arman.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.arman.dto.ReservationDto;
import ru.arman.dto.VehicleDto;
import ru.arman.service.ReservationService;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/reservation")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/create")
    @CircuitBreaker(name = "payment", fallbackMethod = "fallbackPayment")
    @TimeLimiter(name = "payment")
    @Retry(name = "payment")
    public CompletableFuture<String> reserveACar(@RequestBody @Valid ReservationDto reservationDto) {
        return CompletableFuture.supplyAsync(() -> reservationService.reserveCar(reservationDto));
    }

    public CompletableFuture<String> fallbackPayment(Throwable exception) {
        log.error(
                "Service is either unavailable or malfunctioned due to {}", exception.getMessage());
        throw new RuntimeException(exception.getMessage());
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
    @CircuitBreaker(name = "payment", fallbackMethod = "fallbackPayment")
    @TimeLimiter(name = "payment")
    @Retry(name = "payment")
    public CompletableFuture<String> cancelReservation(@PathVariable Long reservationId) {
        return CompletableFuture.supplyAsync(() -> reservationService.cancelReservation(reservationId));
    }
}