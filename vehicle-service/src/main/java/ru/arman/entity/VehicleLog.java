package ru.arman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VehicleLogType type;
    private String description;
    private Date creationDate;

//    to add Vehicle should use getById, not findById
    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;
}
