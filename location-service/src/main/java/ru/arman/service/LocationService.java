package ru.arman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.arman.dto.LocationDto;
import ru.arman.entity.Location;
import ru.arman.entity.Vehicle;
import ru.arman.exception.LocationAlreadyExistsException;
import ru.arman.exception.LocationNotFoundException;
import ru.arman.repository.LocationRepository;
import ru.arman.utils.LocationMapper;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final WebClient.Builder webClientBuilder;

    public List<Location> getAllLocations(String name) {
        return locationRepository.findByName(name);
    }

    public String addLocation(LocationDto locationDto) {
        boolean isLocationsAlreadyExists = locationRepository.existsByName(locationDto.getName());
        if (isLocationsAlreadyExists)
            throw new LocationAlreadyExistsException("Location already exists with name: " + locationDto.getName());

        Location location = locationMapper.mapDtoToLocation(locationDto);
        locationRepository.save(location);

        return "Location " + location.getName() + " added";
    }

    public List<Location> getCountryLocations(String country) {
        return locationRepository.findByAddress_CountryIgnoreCase(country);
    }

    public List<Location> searchCountryLocations(String country, String query) {
        return locationRepository.searchCountryLocations(country.toLowerCase(), query.toLowerCase());
    }

//    public List<Vehicle> getAvailableVehicles(String country,
//                                              String locationName,
//                                              Optional<Date> startDate,
//                                              Optional<Date> endDate,
//                                              Optional<List<String>> carType,
//                                              Optional<List<String>> carClass,
//                                              Optional<Integer> passengerCapacity,
//                                              Optional<Boolean> hasSunRoof,
//                                              Optional<Integer> manufacturingYear) {
//        Optional<Location> location =
//                locationRepository.findByCountryAndName(country.toLowerCase(), locationName.toLowerCase());
//
//        if (location.isEmpty())
//            throw new LocationNotFoundException("No location with name: " + locationName);
//
//        Vehicle[] vehicles = webClientBuilder.build().get()
//                .uri("http://vehicle-service/api/vehicle/all",
//                        uriBuilder -> uriBuilder.queryParam("locationId", location.get().getId())
//                                .queryParam("startDate", startDate)
//                                .queryParam("endDate", endDate)
//                                .queryParam("carType", carType)
//                                .queryParam("carClass", carClass)
//                                .queryParam("passengerCapacity", passengerCapacity)
//                                .queryParam("hasSunRoof", hasSunRoof)
//                                .queryParam("manufacturingYear", manufacturingYear)
//                                .build()
//                )
//                .retrieve()
//                .bodyToMono(Vehicle[].class)
//                .block();
//
//        return List.of(vehicles);
//    }
}
