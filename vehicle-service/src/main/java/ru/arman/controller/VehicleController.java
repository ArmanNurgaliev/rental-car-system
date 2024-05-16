package ru.arman.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.arman.dto.VehicleInputDto;
import ru.arman.entity.Vehicle;
import ru.arman.service.VehicleService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @GetMapping("/all")
    public ResponseEntity<List<Vehicle>> getAllVehicles(
                                @RequestParam Long locationId,
                                @RequestParam Optional<Date> startDate,
                                @RequestParam Optional<Date> endDate,
                                @RequestParam Optional<List<String>> carType,
                                @RequestParam Optional<List<String>> carClass,
                                @RequestParam Optional<Integer> passengerCapacity,
                                @RequestParam Optional<Boolean> hasSunRoof,
                                @RequestParam Optional<Integer> manufacturingYear) {
        return ResponseEntity.ok(vehicleService.getAllVehicles(locationId, startDate, endDate, carType, carClass, passengerCapacity, hasSunRoof, manufacturingYear));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addVehicle(@RequestBody @Valid VehicleInputDto vehicle) {
        return ResponseEntity.ok(vehicleService.addVehicle(vehicle));
    }
}
