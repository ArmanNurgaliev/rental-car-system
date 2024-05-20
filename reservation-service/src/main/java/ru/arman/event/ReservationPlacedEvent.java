package ru.arman.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationPlacedEvent {
    private Long recipientId;
    private String recipientEmail;

    private Long pickUpLocationId;
    private Date dueDate;
}
