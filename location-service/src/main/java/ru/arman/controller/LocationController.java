package ru.arman.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.arman.dto.LocationDto;
import ru.arman.entity.Location;
import ru.arman.entity.Vehicle;
import ru.arman.service.LocationService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/location")
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/{country}")
    public ResponseEntity<List<Location>> getCountryLocations(@PathVariable String country) {
        return ResponseEntity.ok(locationService.getCountryLocations(country));
    }

    @GetMapping("/{country}/search")
    public ResponseEntity<List<Location>> searchCountryLocations(@PathVariable String country,
                                                                 @RequestParam String query) {
        return ResponseEntity.ok(locationService.searchCountryLocations(country, query));
    }

    @GetMapping("/{country}/{location}")
    public ResponseEntity<List<Vehicle>> getAvailableVehicles(@PathVariable String country,
                                                              @PathVariable String location,
                                                              @RequestParam Optional<Date> startDate,
                                                              @RequestParam Optional<Date> endDate,
                                                              @RequestParam Optional<List<String>> carType,
                                                              @RequestParam Optional<List<String>> carClass,
                                                              @RequestParam Optional<Integer> passengerCapacity,
                                                              @RequestParam Optional<Boolean> hasSunRoof,
                                                              @RequestParam Optional<Integer> manufacturingYear) {
        return ResponseEntity.ok(locationService.getAvailableVehicles(country, location, startDate, endDate,
                carType, carClass, passengerCapacity, hasSunRoof, manufacturingYear));
    }

    @PostMapping
    public ResponseEntity<String> addLocation(@RequestBody @Valid LocationDto locationDto) {
        return new ResponseEntity<>(locationService.addLocation(locationDto), HttpStatus.CREATED);
    }
}