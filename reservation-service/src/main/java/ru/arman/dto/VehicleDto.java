package ru.arman.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class VehicleDto {
    @NotNull(message = "Vehicle id can't be null")
    private Long vehicleId;
    @NotNull(message = "Start date can't be null")
    private Date startDate;
    @NotNull(message = "Location id can't be null")
    private Long locationId;
}
