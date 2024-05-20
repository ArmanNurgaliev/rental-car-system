package ru.arman.service;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.arman.dto.VehicleDto;
import ru.arman.dto.VehicleInputDto;
import ru.arman.entity.CarClass;
import ru.arman.entity.CarType;
import ru.arman.entity.Vehicle;
import ru.arman.exception.VehicleAlreadyExistsException;
import ru.arman.exception.VehicleNotFoundException;
import ru.arman.repository.VehicleRepository;
import ru.arman.util.QRCodeGenerator;
import ru.arman.util.VehicleSpecification;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleSpecification vehicleSpecification;
    private final WebClient.Builder webClientBuilder;
    private final QRCodeGenerator qrCodeGenerator;

    public List<Vehicle> getAllVehicles(Long locationId,
                                        Optional<Date> startDate,
                                        Optional<Date> endDate,
                                        Optional<List<String>> carType,
                                        Optional<List<String>> carClass,
                                        Optional<Integer> passengerCapacity,
                                        Optional<Boolean> hasSunRoof,
                                        Optional<Integer> manufacturingYear) {
//        Pageable paging = PageRequest.of(0, 6);
        Specification<Vehicle> spec =
                vehicleSpecification.searchVehicle(locationId, carType, carClass, passengerCapacity, hasSunRoof, manufacturingYear);
        List<Vehicle> vehicles = vehicleRepository.findAll(spec);

        vehicles = getVehiclesReservedInDates(startDate, endDate, vehicles);

        vehicles = getVehiclesChangedLocation(startDate, vehicles);

        vehicles.forEach(v -> {
            try {
                v.setQrCode(qrCodeGenerator.getQRCodeImage("http://localhost:8080/api/vehicle/" + v.getId()));
            } catch (WriterException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        return vehicles;
    }

    private List<Vehicle> getVehiclesChangedLocation(Optional<Date> startDate, List<Vehicle> vehicles) {
        List<VehicleDto> vehicleDtos = vehicles.stream().map(vehicle -> mapToVehicleDto(vehicle, startDate)).toList();
        Long[] changedLocationVehicles = webClientBuilder.build().post()
                .uri("http://reservation-service/api/reservation/changed-location")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(vehicleDtos), new ParameterizedTypeReference<List<Vehicle>>() {})
                .retrieve()
                .bodyToMono(Long[].class)
                .block();

        vehicles = vehicles.stream()
                .filter(v -> !List.of(changedLocationVehicles).contains(v.getId()))
                .toList();

        return vehicles;
    }

    private List<Vehicle> getVehiclesReservedInDates(Optional<Date> startDate, Optional<Date> endDate, List<Vehicle> vehicles) {
        Long[] reservedVehiclesIds = webClientBuilder.build().get()
                .uri("http://reservation-service/api/reservation/overlapping",
                        uriBuilder -> uriBuilder
                                .queryParam("startDate", startDate.orElseGet(() -> new Date(System.currentTimeMillis())))
                                .queryParam("endDate", endDate.orElseGet(() -> Date.valueOf(LocalDate.now().plusDays(7))))
                                .build())
                .retrieve()
                .bodyToMono(Long[].class)
                .block();

        vehicles = vehicles.stream()
                .filter(v -> !List.of(reservedVehiclesIds).contains(v.getId()))
                .toList();
        return vehicles;
    }

    private VehicleDto mapToVehicleDto(Vehicle vehicle, Optional<Date> startDate) {
        return VehicleDto.builder()
                .vehicleId(vehicle.getId())
                .startDate(startDate.orElseGet(() -> new Date(System.currentTimeMillis())))
                .locationId(vehicle.getLocationId())
                .build();
    }

    public String addVehicle(VehicleInputDto vehicleInputDto) {
        vehicleRepository.findByLicenseNumber(vehicleInputDto.getLicenseNumber())
                .ifPresent(v -> {
                    throw new VehicleAlreadyExistsException("Vehicle already exists with license number: " + vehicleInputDto.getLicenseNumber());
                });

        Vehicle vehicle = mapDtoToVehicle(vehicleInputDto);

        vehicle.setStockNumber(generateStockNumber());
        // TODO: generate barcode
        vehicleRepository.save(vehicle);

        return "Vehicle: " + vehicle.getName() + " added";
    }

    private Vehicle mapDtoToVehicle(VehicleInputDto vehicleInputDto) {
        return Vehicle.builder()
                .name(vehicleInputDto.getName())
                .licenseNumber(vehicleInputDto.getLicenseNumber())
                .priceForDay(vehicleInputDto.getPriceForDay())
                .passengerCapacity(vehicleInputDto.getPassengerCapacity())
                .hasSunRoof(vehicleInputDto.isHasSunRoof())
                .make(vehicleInputDto.getMake())
                .model(vehicleInputDto.getModel())
                .manufacturingYear(vehicleInputDto.getManufacturingYear())
                .mileage(vehicleInputDto.getMileage())
                .carClass(CarClass.valueOf(vehicleInputDto.getCarClass()))
                .carType(CarType.valueOf(vehicleInputDto.getCarType()))
                .locationId(vehicleInputDto.getLocationId())
                .build();
    }

    private String generateStockNumber() {
        String stockNumber = UUID.randomUUID().toString();
        while (vehicleRepository.existsByStockNumber(stockNumber))
            stockNumber = UUID.randomUUID().toString();
        return stockNumber;
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + id));
    }
}
