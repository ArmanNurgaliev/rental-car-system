package ru.arman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.arman.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
