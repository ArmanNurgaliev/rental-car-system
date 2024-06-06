package ru.arman.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationDto {
    @NotBlank(message = "Name can't be empty")
    private String name;
    @NotBlank(message = "Street address can't be empty")
    private String streetAddress;
    @NotBlank(message = "City name can't be empty")
    private String city;
    @NotBlank(message = "Zip code can't be empty")
    private String zipCode;
    @NotBlank(message = "State name can't be empty")
    private String state;
    @NotBlank(message = "Country name can't be empty")
    private String country;
}