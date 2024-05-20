package ru.arman.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import ru.arman.entity.Notification;
import ru.arman.event.ReservationPlacedEvent;
import ru.arman.repository.NotificationRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @KafkaListener(topics = "reservation")
    public void sendNotificationAboutRentingACar(ReservationPlacedEvent reservationPlacedEvent) {
        Notification notification = Notification.builder()
                .recipientId(reservationPlacedEvent.getRecipientId())
                .recipientEmail(reservationPlacedEvent.getRecipientEmail())
                .timestamp(LocalDateTime.now())
                .message("You reserved a car. It will wait you: " + reservationPlacedEvent.getDueDate())
                .build();

        notificationRepository.save(notification);

        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(notification.getRecipientEmail()));
            mimeMessage.setFrom(new InternetAddress("rentalcar@mycompany.example"));
            mimeMessage.setText(notification.getMessage());
        };

        try {
            this.mailSender.send(preparator);
        }
        catch (MailException ex) {
            System.err.println(ex.getMessage());
        }


        System.out.println(notification.getMessage());
    }
}
