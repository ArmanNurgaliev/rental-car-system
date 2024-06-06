package ru.arman.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.arman.TestDataConfiguration;
import ru.arman.dto.VehicleDto;

import java.sql.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestDataConfiguration.class)
@Sql(scripts={"classpath:reservations.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class ReservationControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void getReservedVehiclesTest_shouldReturnList() throws Exception {
        Date startDate = Date.valueOf("2024-05-10");
        Date endDate = Date.valueOf("2024-05-17");

        mvc.perform(get("/api/reservation/overlapping")
                        .queryParam("startDate", String.valueOf(startDate))
                        .queryParam("endDate", String.valueOf(endDate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$", hasItems(1, 2, 3)));
    }

    @Test
    void getChangedLocationVehiclesTest_shouldReturnList() throws Exception {
        Date returnDate = Date.valueOf("2024-05-21");
        VehicleDto vehicleDto = VehicleDto.builder()
                .vehicleId(2L)
                .locationId(1L)
                .startDate(returnDate)
                .build();

        mvc.perform(post("/api/reservation/changed-location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(List.of(vehicleDto))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0]").value(vehicleDto.getVehicleId()));
    }
}