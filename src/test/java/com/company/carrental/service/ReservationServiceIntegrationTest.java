package com.company.carrental.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.checkerframework.checker.units.qual.s;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import com.company.carrental.dto.CarDTO;
import com.company.carrental.dto.ReservationDTO;
import com.company.carrental.dto.UserDTO;
import com.company.carrental.entity.Car;
import com.company.carrental.entity.CarType;
import com.company.carrental.entity.Reservation;
import com.company.carrental.entity.User;
import com.company.carrental.entity.CarType.VechicleType;
import com.company.carrental.repository.CarRepository;
import com.company.carrental.repository.ReservationRepository;
import com.company.carrental.repository.UserRepository;

@SpringBootTest
public class ReservationServiceIntegrationTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CarService carService;
    @Mock
    private UserService userService;

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationService = new ReservationService(
                reservationRepository,
                carRepository,
                userRepository,
                carService,
                userService);
        // Common mocks
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(createTestUser(1)));
        when(userService.getUserById(anyInt())).thenReturn(createTestUserDTO(1));
    }

    @Test
    void shouldSuccessfullyCreateMultipleReservationsAcrossMonths() {
        // Setup test data
        UserDTO userDTO = createTestUserDTO(1);
        CarDTO carDTO = createTestCarDTO(1);
        Car car = createTestCar(carDTO.getCarId(), Car.CarStatus.AVAILABLE);
        User user = createTestUser(userDTO.getUserId());

        // Create multiple reservations across different months
        ReservationDTO octReservation = createReservationDTO(
                LocalDate.of(2023, 10, 1),
                LocalDate.of(2023, 10, 3),
                carDTO,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        ReservationDTO novReservation = createReservationDTO(
                LocalDate.of(2023, 11, 1),
                LocalDate.of(2023, 11, 3),
                carDTO,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        Reservation savedOctReservation = new Reservation(user, car, octReservation.getStartDate(),
                octReservation.getEndDate(), octReservation.getStatus());
        Reservation savedNovReservation = new Reservation(user, car, novReservation.getStartDate(),
                novReservation.getEndDate(), novReservation.getStatus());

        // Mock repository behavior
        when(carRepository.findById(car.getCarId())).thenReturn(Optional.of(car));
        when(carService.getCarById(carDTO.getCarId())).thenReturn(carDTO);
        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(savedOctReservation, savedNovReservation);
        // Execute and verify
        ReservationDTO createdOct = reservationService.createReservation(octReservation);
        ReservationDTO createdNov = reservationService.createReservation(novReservation);

        assertNotNull(createdOct);
        assertNotNull(createdNov);
        assertEquals(Reservation.ReservationStatus.ACTIVE, createdOct.getStatus());
        assertEquals(Reservation.ReservationStatus.ACTIVE, createdNov.getStatus());
    }

    @Test
    void shouldFailWhenCreatingOverlappingReservations() {
        // Setup test data
        UserDTO userDTO = createTestUserDTO(1);
        CarDTO carDTO = createTestCarDTO(1);
        Car car = createTestCar(carDTO.getCarId(), Car.CarStatus.AVAILABLE);
        User user = createTestUser(userDTO.getUserId());

        // Create overlapping reservations
        ReservationDTO existingReservationDTO = createReservationDTO(
                LocalDate.of(2023, 10, 1),
                LocalDate.of(2023, 10, 5),
                carDTO,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        ReservationDTO overlappingReservationDTO = createReservationDTO(
                LocalDate.of(2023, 10, 3),
                LocalDate.of(2023, 10, 7),
                carDTO,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        Reservation existingReservation = new Reservation(user, car, existingReservationDTO.getStartDate(),
                existingReservationDTO.getEndDate(), existingReservationDTO.getStatus());
        // Mock repository behavior
        when(carRepository.findById(car.getCarId())).thenReturn(Optional.of(car));
        when(carService.getCarById(carDTO.getCarId())).thenReturn(carDTO);

        // Simple mock that always returns the same test data
        when(reservationRepository.findByCarAndStatus(any(), any())).thenReturn(Arrays.asList(existingReservation));

        when(reservationRepository.findByCarAndStatus(eq(car), eq(Reservation.ReservationStatus.ACTIVE)))
                .thenReturn(Arrays.asList(existingReservation));

        // Execute and verify
        assertThrows(RuntimeException.class, () -> {
            reservationService.createReservation(existingReservationDTO);
            reservationService.createReservation(overlappingReservationDTO);
        });
    }

    @Test
    void shouldHandleMultipleReservationsForDifferentCars() {
        // Setup test data for two different cars
        UserDTO userDTO = createTestUserDTO(1);
        CarDTO carDTO1 = createTestCarDTO(1);
        CarDTO carDTO2 = createTestCarDTO(2);
        Car car1 = createTestCar(carDTO1.getCarId(), carDTO1.getStatus());
        Car car2 = createTestCar(carDTO2.getCarId(), carDTO2.getStatus());
        User user = createTestUser(1);

        // Create simultaneous reservations for different cars
        ReservationDTO reservation1 = createReservationDTO(
                LocalDate.of(2023, 10, 1),
                LocalDate.of(2023, 10, 3),
                carDTO1,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        ReservationDTO reservation2 = createReservationDTO(
                LocalDate.of(2023, 10, 1),
                LocalDate.of(2023, 10, 3),
                carDTO2,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        Reservation savedReservation1 = new Reservation(user, car1, reservation1.getStartDate(),
                reservation1.getEndDate(), reservation1.getStatus());
        Reservation savedReservation2 = new Reservation(user, car2, reservation2.getStartDate(),
                reservation2.getEndDate(), reservation2.getStatus());

        // Mock repository behavior
        when(carRepository.findById(car1.getCarId())).thenReturn(Optional.of(car1));
        when(carRepository.findById(car2.getCarId())).thenReturn(Optional.of(car2));
        when(carService.getCarById(carDTO1.getCarId())).thenReturn(carDTO1);
        when(carService.getCarById(carDTO2.getCarId())).thenReturn(carDTO2);

        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation1, savedReservation2);

        // Execute and verify
        ReservationDTO created1 = reservationService.createReservation(reservation1);
        ReservationDTO created2 = reservationService.createReservation(reservation2);

        assertNotNull(created1);
        assertNotNull(created2);
        assertNotEquals(created1.getCar().getCarId(), created2.getCar().getCarId());
    }

    private UserDTO createTestUserDTO(Integer userId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setFirstName("Test" + userId);
        userDTO.setLastName("User" + userId);
        return userDTO;
    }

    private CarDTO createTestCarDTO(Integer carId) {
        CarDTO carDTO = new CarDTO();
        carDTO.setCarId(carId);
        carDTO.setStatus(Car.CarStatus.AVAILABLE);
        return carDTO;
    }

    private Car createTestCar(Integer carId, Car.CarStatus status) {
        Car car = new Car();
        car.setCarId(carId);
        car.setStatus(status);
        CarType carType = new CarType();
        carType.setCarTypeId(1);
        carType.setVehicleType(VechicleType.SEDAN);
        car.setCarType(carType);
        return car;
    }

    private User createTestUser(Integer userId) {
        User user = new User();
        user.setUserId(userId);
        user.setFirstName("Test" + userId);
        user.setLastName("User" + userId);
        user.setUsername("testuser" + userId);
        user.setPassword("password" + userId);
        return user;
    }

    private ReservationDTO createReservationDTO(
            LocalDate startDate,
            LocalDate endDate,
            CarDTO carDTO,
            UserDTO userDTO,
            Reservation.ReservationStatus status) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartDate(startDate);
        reservationDTO.setEndDate(endDate);
        reservationDTO.setCar(carDTO);
        reservationDTO.setUser(userDTO);
        reservationDTO.setStatus(status);
        return reservationDTO;
    }
}
