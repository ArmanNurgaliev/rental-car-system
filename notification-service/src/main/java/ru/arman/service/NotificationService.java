package ru.arman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.arman.entity.Notification;
import ru.arman.event.ReservationPlacedEvent;
import ru.arman.repository.NotificationRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "reservation")
    public void sendNotificationAboutRentingACar(ReservationPlacedEvent reservationPlacedEvent) {
        Notification notification = Notification.builder()
                .recipientId(reservationPlacedEvent.getRecipientId())
                .timestamp(LocalDateTime.now())
                .message("You reserved a car. It will wait you: " + reservationPlacedEvent.getDueDate())
                .build();

        notificationRepository.save(notification);

//       TODO Send email or sms
        System.out.println(notification.getMessage());
    }
}
