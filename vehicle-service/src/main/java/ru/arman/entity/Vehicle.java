package ru.arman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String licenseNumber;
    private String stockNumber;

    private BigDecimal priceForDay;

    private int passengerCapacity;
    private String barCode;
    private boolean hasSunRoof;
    private String make;
    private String model;
    private int manufacturingYear;
    private int mileage;
    @Enumerated(EnumType.STRING)
    private CarClass carClass;
    @Enumerated(EnumType.STRING)
    private CarType carType;

//    @Enumerated(EnumType.STRING)а
//    private VehicleStatus status;

    private Long locationId;
}
