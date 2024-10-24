package com.company.carrental.service;

import com.company.carrental.dto.ReservationDTO;
import com.company.carrental.entity.Car;
import com.company.carrental.entity.Reservation;
import com.company.carrental.entity.User;
import com.company.carrental.repository.CarRepository;
import com.company.carrental.repository.ReservationRepository;
import com.company.carrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDate;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarService carService;
    private final UserService userService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
            CarRepository carRepository,
            UserRepository userRepository,
            CarService carService,
            UserService userService) {
        this.reservationRepository = reservationRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.carService = carService;
        this.userService = userService;
    }

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        User user = userRepository.findById(reservationDTO.getUser().getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Car car = carRepository.findById(reservationDTO.getCar().getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));
        System.out
                .println("*** Checking for existing reservations for the car:" + reservationDTO.getCar() + " dates: "
                        + reservationDTO.getStartDate() + " "
                        + reservationDTO.getEndDate());
        List<Reservation> existingReservations = findExistingActiveReservationsForCar(car.getCarId());

        boolean hasOverlap = existingReservations.stream()
                .filter(r -> r.getStatus() == Reservation.ReservationStatus.ACTIVE)
                .anyMatch(existing -> {
                    return reservationDTO.getStartDate().isBefore(existing.getEndDate()) &&
                            reservationDTO.getEndDate().isAfter(existing.getStartDate());
                });

        if (hasOverlap) {
            throw new RuntimeException(
                    "Car is already reserved for the selected dates: starting:" + reservationDTO.getStartDate()
                            + " ending:"
                            + reservationDTO.getEndDate() + " " + car.toString());
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCar(car);
        reservation.setStartDate(reservationDTO.getStartDate());
        reservation.setEndDate(reservationDTO.getEndDate());
        reservation.setStatus(Reservation.ReservationStatus.ACTIVE);

        if (reservation.getStartDate().isEqual(LocalDate.now())) {
            car.setStatus(Car.CarStatus.RESERVED);
        } else {
            car.setStatus(Car.CarStatus.AVAILABLE);
        }
        carRepository.save(car);

        Reservation savedReservation = reservationRepository.save(reservation);
        return convertToDTO(savedReservation);
    }

    public List<Reservation> findExistingActiveReservationsForCar(Integer carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));
        return reservationRepository.findByCarAndStatus(car, Reservation.ReservationStatus.ACTIVE);
    }

    public ReservationDTO getReservationById(Integer id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        return convertToDTO(reservation);
    }

    public List<ReservationDTO> getUserReservations(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Reservation> reservations = reservationRepository.findByUser(user);
        return reservations.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public ReservationDTO updateReservation(Integer id, ReservationDTO reservationDTO) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStartDate(reservationDTO.getStartDate());
        reservation.setEndDate(reservationDTO.getEndDate());
        reservation.setStatus(reservationDTO.getStatus());

        Reservation updatedReservation = reservationRepository.save(reservation);
        return convertToDTO(updatedReservation);
    }

    @Transactional
    public void cancelReservation(Integer id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        Car car = reservation.getCar();
        car.setStatus(Car.CarStatus.AVAILABLE);
        carRepository.save(car);
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(reservation.getReservationId());
        dto.setStatus(reservation.getStatus());
        dto.setStartDate(reservation.getStartDate());
        dto.setEndDate(reservation.getEndDate());
        dto.setUser(userService.getUserById(reservation.getUser().getUserId()));
        dto.setCar(carService.getCarById(reservation.getCar().getCarId()));
        return dto;
    }
}