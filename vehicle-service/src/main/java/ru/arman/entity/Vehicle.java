package ru.arman.entity;

import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    @Transient
    private byte[] qrCode;
    private boolean hasSunRoof;
    private String make;
    private String model;
    private int manufacturingYear;
    private int mileage;
    @Enumerated(EnumType.STRING)
    private CarClass carClass;
    @Enumerated(EnumType.STRING)
    private CarType carType;

    private Long locationId;
}
