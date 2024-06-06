package ru.arman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.arman.dto.LocationDto;
import ru.arman.entity.Location;
import ru.arman.exception.LocationAlreadyExistsException;
import ru.arman.repository.LocationRepository;
import ru.arman.utils.LocationMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;


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
}