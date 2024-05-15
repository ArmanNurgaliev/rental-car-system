package ru.arman.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String streetAddress;
    private String city;
    private String zipCode;
    private String state;
    private String country;
}
