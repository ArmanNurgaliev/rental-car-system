package ru.arman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.arman.dto.PaymentDto;
import ru.arman.dto.PaymentResponseDto;
import ru.arman.dto.ReservationDto;
import ru.arman.dto.VehicleDto;
import ru.arman.entity.Reservation;
import ru.arman.entity.ReservationStatus;
import ru.arman.event.ReservationPlacedEvent;
import ru.arman.exception.ReservationDatesException;
import ru.arman.exception.ReservationNotFoundException;
import ru.arman.exception.VehicleAlreadyReservedException;
import ru.arman.repository.ReservationRepository;
import ru.arman.utils.ReservationMapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, ReservationPlacedEvent> template;

    @Transactional
    public String reserveCar(ReservationDto reservationDto) {
        isCorrectReservation(reservationDto);

        Reservation reservation = reservationMapper.mapDtoToReservation(reservationDto);
        reservation.setCreationDate(new Date(System.currentTimeMillis()));

        PaymentDto paymentDto = createPaymentDto(reservationDto);
        PaymentResponseDto paymentResponseDto = webClientBuilder.build().post()
                .uri("http://payment-service/api/payment/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(paymentDto), PaymentDto.class)
                .retrieve()
                .bodyToMono(PaymentResponseDto.class)
                .block();

        reservation.setPaymentId(paymentResponseDto.getId());
        reservation.setStatus(ReservationStatus.COMPLETED);

        reservationRepository.save(reservation);

        ReservationPlacedEvent reservationPlacedEvent =
                new ReservationPlacedEvent(reservation.getCustomerId(), reservationDto.getCustomerEmail(), reservation.getPickUpLocationId(), reservation.getDueDate());
        template.send("reservation", reservationPlacedEvent);

        return "You reserved a car. It will wait you: " + reservation.getDueDate();
    }

    private void isCorrectReservation(ReservationDto reservationDto) {
        if (reservationDto.getDueDate().before(new Date(System.currentTimeMillis())))
            throw new ReservationDatesException("Can't reserve a vehicle in past");

        if (reservationDto.getDueDate().after(reservationDto.getReturnDate()))
            throw new ReservationDatesException("Wrong reservation dates");

        Optional<Reservation> isReserved =
                reservationRepository.findReservationByVehicleAndDates(reservationDto.getVehicleId(), reservationDto.getDueDate(), reservationDto.getReturnDate());
        if (isReserved.isPresent())
            throw new VehicleAlreadyReservedException("Vehicle is reserved at this dates");
    }

    private PaymentDto createPaymentDto(ReservationDto reservationDto) {
        return PaymentDto.builder()
                .cardHolder(reservationDto.getCardHolder())
                .cardNumber(reservationDto.getCardNumber())
                .cvc(reservationDto.getCvc())
                .dateValue(reservationDto.getDateValue())
                .priceForDay(reservationDto.getPriceForDay())
                .dueDate(reservationDto.getDueDate())
                .returnDate(reservationDto.getReturnDate())
                .customerId(reservationDto.getCustomerId())
                .build();
    }

    public List<Long> getReservedVehicles(Date startDate, Date endDate) {
        return reservationRepository.findOverlappingReservations(startDate, endDate)
                .stream().map(Reservation::getVehicleId)
                .toList();
    }

    public List<Long> getChangedLocationVehicles(List<VehicleDto> vehicles) {
        List<Long> changedLocationVehicles = new ArrayList<>();
        for (VehicleDto v: vehicles) {
            Optional<Reservation> reservation =
                    reservationRepository.findVehiclesChangedLocation(v.getVehicleId(), v.getStartDate(), v.getLocationId());
            reservation.ifPresent(value -> changedLocationVehicles.add(value.getVehicleId()));
        }

        return changedLocationVehicles;
    }

    public String cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with id: " + reservationId));

        String deletedPayment = webClientBuilder.build().post()
                .uri("http://payment-service/api/payment/cancel/" + reservation.getPaymentId())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        reservationRepository.deleteById(reservationId);

        return "Reservation cancelled. ";
    }
}
