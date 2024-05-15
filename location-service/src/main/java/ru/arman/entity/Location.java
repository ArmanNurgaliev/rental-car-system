package ru.arman.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.arman.dto.UserDto;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Address address;

    // Administrators who works in this location
//    @Transient
//    private List<UserDto> administrators;

}
