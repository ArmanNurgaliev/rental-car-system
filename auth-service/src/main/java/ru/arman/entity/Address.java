package ru.arman.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Address {

    private String streetAddress;
    private String city;
    private String zipCode;
    private String state;
    private String country;
}
