package ru.arman.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.arman.dto.VehicleInputDto;
import ru.arman.entity.Vehicle;
import ru.arman.service.VehicleService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
@Slf4j
public class VehicleController {
    private final VehicleService vehicleService;

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @GetMapping("/all")
    @CircuitBreaker(name = "reservation", fallbackMethod = "fallbackReservation")
    @TimeLimiter(name = "reservation")
    @Retry(name = "reservation")
    public CompletableFuture<List<Vehicle>> getAllVehicles(
                                @RequestParam Long locationId,
                                @RequestParam Optional<Date> startDate,
                                @RequestParam Optional<Date> endDate,
                                @RequestParam Optional<List<String>> carType,
                                @RequestParam Optional<List<String>> carClass,
                                @RequestParam Optional<Integer> passengerCapacity,
                                @RequestParam Optional<Boolean> hasSunRoof,
                                @RequestParam Optional<Integer> manufacturingYear) {

        return CompletableFuture.supplyAsync(() -> vehicleService.getAllVehicles(locationId, startDate, endDate, carType, carClass, passengerCapacity, hasSunRoof, manufacturingYear));
    }

    public CompletableFuture<List<Vehicle>> fallbackReservation(Throwable exception) {
        log.error(
                "Service is either unavailable or malfunctioned due to {}", exception.getMessage());
        throw new RuntimeException(exception.getMessage());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addVehicle(@RequestBody @Valid VehicleInputDto vehicle) {
        return ResponseEntity.ok(vehicleService.addVehicle(vehicle));
    }
}
