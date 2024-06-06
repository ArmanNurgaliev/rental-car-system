package ru.arman.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.arman.TestDataConfiguration;
import ru.arman.dto.LocationDto;
import ru.arman.entity.Address;
import ru.arman.entity.Location;
import ru.arman.repository.LocationRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestDataConfiguration.class)
@Transactional
class LocationControllerTest {
    @Autowired
    private MockMvc mvc;

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

    @Test
    void getCountryLocations() throws Exception {
        mvc.perform(get("/api/location/" + location.getAddress().getCountry()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(location.getName()));
    }

    @Test
    void searchCountryLocations() throws Exception {
        mvc.perform(get("/api/location/" + location.getAddress().getCountry() + "/search")
                        .param("query", location.getAddress().getStreetAddress()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(location.getName()));
    }

    @Test
    void addLocation() throws Exception {
        LocationDto locationDto = LocationDto.builder()
                .country("Egypt")
                .name("Egypt Cairo")
                .city("Cairo")
                .streetAddress("Main street")
                .zipCode("5486168")
                .state("Cairo")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(post("/api/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("Location " + locationDto.getName() + " added"));
    }
}