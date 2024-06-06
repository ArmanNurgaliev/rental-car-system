package ru.arman.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.arman.dto.LocationDto;
import ru.arman.entity.Address;
import ru.arman.entity.Location;
import ru.arman.exception.LocationAlreadyExistsException;
import ru.arman.repository.LocationRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class LocationServiceTest {
    @Autowired
    private LocationService locationService;

    @MockBean
    private LocationRepository locationRepository;

    private LocationDto locationDto;
    private Location location1;
    private Location location2;

    @BeforeEach
    void setUp() {
        locationDto = LocationDto.builder()
                .name("Tomsk address")
                .streetAddress("Lenina 5")
                .city("Tomsk")
                .zipCode("654832")
                .state("Tomskaya oblast'")
                .country("Russia")
                .build();

        location1 = Location.builder()
                .name("Name 1")
                .address(new Address())
                .build();

        location2 = Location.builder()
                .name("Name 2")
                .address(new Address())
                .build();
    }

    @Test
    public void addLocationTest_shouldReturnString() {
        String answer = locationService.addLocation(locationDto);

        assertEquals("Location " + locationDto.getName() + " added", answer);
    }

    @Test
    public void addLocationTest_shouldThrowException() {
        when(locationRepository.existsByName(any())).thenReturn(true);

        LocationAlreadyExistsException exception =
                assertThrows(LocationAlreadyExistsException.class, () -> locationService.addLocation(locationDto));

        assertEquals("Location already exists with name: Tomsk address", exception.getMessage());
    }

    @Test
    public void getCountryLocationsTest_shouldReturnLocations() {
        when(locationRepository.findByAddress_CountryIgnoreCase(any())).thenReturn(List.of(location1, location2));

        List<Location> locations = locationService.getCountryLocations("Russia");

        assertEquals(2, locations.size());
        assertEquals(location1, locations.get(0));
    }

    @Test
    public void searchCountryLocationsTest_shouldReturnLocation() {
        when(locationRepository.searchCountryLocations(any(), any())).thenReturn(List.of(location1));

        List<Location> locations = locationService.searchCountryLocations("Russia", "Tomsk");

        assertEquals(1, locations.size());
        assertEquals(location1, locations.get(0));
    }
}