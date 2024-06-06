package ru.arman.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.arman.entity.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>,
        JpaSpecificationExecutor<Vehicle> {
    Optional<Vehicle> findByLicenseNumber(String licenseNumber);

    List<Vehicle> findAll(Specification<Vehicle> specification);

    boolean existsByStockNumber(String stockNumber);
}
