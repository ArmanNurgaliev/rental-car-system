package ru.arman.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.arman.TestDataConfiguration;
import ru.arman.entity.Address;
import ru.arman.entity.Location;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestDataConfiguration.class)
class LocationRepositoryTest {
    @Autowired
    private LocationRepository locationRepository;
    private Location location;

    @BeforeEach
    void setUp() {
        Address address = Address.builder()
                .country("Russia")
                .streetAddress("Lenina 5")
                .city("Tomsk")
                .build();
        location = Location.builder()
                .name("Location 1")
                .address(address)
                .build();

        locationRepository.save(location);
    }

    @AfterEach
    void tearDown() {
        locationRepository.delete(location);
    }
    @Test
    void existsByName_shouldReturnTrue() {
        boolean exists = locationRepository.existsByName(location.getName());

        assertTrue(exists);
    }
    @Test
    void existsByName_shouldReturnFalse() {
        boolean exists = locationRepository.existsByName("Athens");

        assertFalse(exists);
    }

    @Test
    void findByAddress_CountryIgnoreCase() {
        List<Location> locations =
                locationRepository.findByAddress_CountryIgnoreCase(location.getAddress().getCountry());

        assertEquals(1, locations.size());
        assertEquals(location.getName(), locations.get(0).getName());
    }

    @Test
    void searchCountryLocations() {
        List<Location> locations =
                locationRepository.searchCountryLocations(location.getAddress().getCountry().toLowerCase(),
                        location.getName().toLowerCase());

        assertEquals(1, locations.size());
        assertEquals(location.getName(), locations.get(0).getName());
    }
}