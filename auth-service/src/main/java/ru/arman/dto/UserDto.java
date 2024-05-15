package ru.arman.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {
    @NotBlank(message = "Name can't be empty")
    private String name;
    @NotBlank(message = "Email can't be empty")
    @Email
    private String email;
    @Size(min = 4, message = "Weak password")
    private String password;
    @Pattern(regexp = "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$",
             message = "Phone number is not valid")
    private String phone;
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
