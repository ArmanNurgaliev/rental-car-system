package ru.arman.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date creationDate;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private Date dueDate;
    private Date returnDate;
    private Long pickUpLocationId;
    private Long returnLocationId;
    private Long customerId;
    private Long vehicleId;
    private Long paymentId;
}
