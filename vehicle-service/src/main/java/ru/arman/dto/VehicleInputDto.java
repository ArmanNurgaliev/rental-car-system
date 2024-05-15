package ru.arman.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.arman.entity.CarClass;
import ru.arman.entity.CarType;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class VehicleInputDto {
    @NotBlank(message = "Name can't be empty")
    private String name;
    @NotBlank(message = "License number can't be empty")
    @Pattern(regexp = "^[A-Z][0-9]{3}[A-Z]{2}$",
    message = "License number is not valid")
    private String licenseNumber;

    @DecimalMin(value = "0.0")
    private BigDecimal priceForDay;

    @Min(value = 0, message = "Passenger capacity can't be less than 0")
    private int passengerCapacity;
    @NotNull(message = "Has sun roof can't be null")
    private boolean hasSunRoof;
    @NotBlank(message = "Make can't be empty")
    private String make;
    @NotBlank(message = "Model can't be empty")
    private String model;
    @NotNull(message = "Manufacturing year can't be null")
    private int manufacturingYear;
    @NotNull(message = "Mileage can't be null")
    private int mileage;
    @NotBlank(message = "Car Class can't be empty")
    private String carClass;
    @NotBlank(message = "Car Type can't be empty")
    private String carType;
    @NotNull(message = "Location id can't be null")
    private Long locationId;
}
