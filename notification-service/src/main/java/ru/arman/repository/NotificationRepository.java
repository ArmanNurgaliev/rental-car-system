package ru.arman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.arman.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
