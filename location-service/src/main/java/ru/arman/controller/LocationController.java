package ru.arman.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.arman.dto.LocationDto;
import ru.arman.entity.Location;
import ru.arman.service.LocationService;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<String> addLocation(@RequestBody @Valid LocationDto locationDto) {
        return new ResponseEntity<>(locationService.addLocation(locationDto), HttpStatus.CREATED);
    }
}