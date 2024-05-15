package ru.arman.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Builder
public class VehicleDto {
    private Long vehicleId;
    private Date startDate;
    private Long locationId;
}
