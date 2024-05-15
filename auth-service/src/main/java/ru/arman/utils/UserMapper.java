package ru.arman.utils;

import org.springframework.stereotype.Component;
import ru.arman.dto.UserDto;
import ru.arman.entity.Address;
import ru.arman.entity.User;

@Component
public class UserMapper {
    public User mapDtoToUser(UserDto userDto) {
        Address address = Address.builder()
                .country(userDto.getCountry())
                .state(userDto.getState())
                .zipCode(userDto.getZipCode())
                .city(userDto.getCity())
                .streetAddress(userDto.getStreetAddress())
                .build();

        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phone(userDto.getPhone())
                .address(address)
                .build();
    }
}
