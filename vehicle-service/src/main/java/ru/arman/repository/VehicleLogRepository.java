package ru.arman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.arman.entity.VehicleLog;

public interface VehicleLogRepository extends JpaRepository<VehicleLog, Long> {
}
