package ru.arman.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vehicle {
    private Long id;
    private String name;

    private String licenseNumber;
    private String stockNumber;

    private int passengerCapacity;
    private String barCode;
    private boolean hasSunRoof;
    private String make;
    private String model;
    private String manufacturingYear;
    private int mileage;
    @Enumerated(EnumType.STRING)
    private CarClass carClass;
    @Enumerated(EnumType.STRING)
    private CarType carType;
    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    private Long locationId;
}
