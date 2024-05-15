package ru.arman.utils;

import org.springframework.stereotype.Component;
import ru.arman.dto.LocationDto;
import ru.arman.entity.Address;
import ru.arman.entity.Location;

@Component
public class LocationMapper {
    public Location mapDtoToLocation(LocationDto locationDto) {
        Address address = Address.builder()
                .country(locationDto.getCountry())
                .state(locationDto.getState())
                .zipCode(locationDto.getZipCode())
                .city(locationDto.getCity())
                .streetAddress(locationDto.getStreetAddress())
                .build();

        return Location.builder()
                .name(locationDto.getName())
                .address(address)
                .build();
    }
}
