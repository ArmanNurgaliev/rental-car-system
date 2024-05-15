package ru.arman.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private AccountStatus status;

    private Long locationId;

    private int totalVehiclesReserved;
}
