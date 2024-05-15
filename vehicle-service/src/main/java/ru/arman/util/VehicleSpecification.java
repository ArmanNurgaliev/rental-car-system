package ru.arman.util;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.arman.entity.CarClass;
import ru.arman.entity.CarType;
import ru.arman.entity.Vehicle;

import java.util.List;
import java.util.Optional;

@Component
public class VehicleSpecification {
    public Specification<Vehicle> locationId(long id) {
        return (root, query, cb) -> cb.equal(root.get("locationId"), id);
    }
    public Specification<Vehicle> passengerCapacityIs(int capacity) {
        return (root, query, cb) ->
            cb.equal(
                    root.get("passengerCapacity"),
                    capacity
            );
    }

    public Specification<Vehicle> hasSunRoof() {
        return (root, query, cb) -> cb.isTrue(root.get("hasSunRoof"));
    }

    public Specification<Vehicle> manufacturingYearAfter(int manufacturingYear) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("manufacturingYear"), manufacturingYear);
    }

    public Specification<Vehicle> carTypeIs(List<CarType> carTypes) {
        return (root, query, cb) -> root.get("carType").in(carTypes);
    }

    public Specification<Vehicle> carClassIs(List<CarClass> carClasses) {
        return (root, query, cb) -> root.get("carClass").in(carClasses);
    }

    public Specification<Vehicle> searchVehicle(Long locationId,
                                                Optional<List<String>> carType,
                                                Optional<List<String>> carClass,
                                                Optional<Integer> passengerCapacity,
                                                Optional<Boolean> hasSunRoof,
                                                Optional<Integer> manufacturingYear) {
        Specification<Vehicle> spec = Specification.where(locationId(locationId));

        if (carType.isPresent() && !carType.get().isEmpty())
            spec = spec.and(carTypeIs(carType.get().stream().map(CarType::valueOf).toList()));

        if (carClass.isPresent() && !carClass.get().isEmpty()) {
            spec = spec.and(carClassIs(carClass.get().stream().map(CarClass::valueOf).toList()));
        }

        if (passengerCapacity.isPresent())
            spec = spec.and(passengerCapacityIs(passengerCapacity.get()));

        if (hasSunRoof.isPresent() && hasSunRoof.get())
            spec = spec.and(hasSunRoof());

        if (manufacturingYear.isPresent())
            spec = spec.and(manufacturingYearAfter(manufacturingYear.get()));

        return spec;
    }
}
