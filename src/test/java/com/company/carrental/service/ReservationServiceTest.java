package com.company.carrental.service;

import com.company.carrental.dto.CarDTO;
import com.company.carrental.dto.ReservationDTO;
import com.company.carrental.dto.UserDTO;
import com.company.carrental.entity.Car;
import com.company.carrental.entity.CarType;
import com.company.carrental.entity.CarType.VechicleType;
import com.company.carrental.entity.Reservation;
import com.company.carrental.entity.User;
import com.company.carrental.repository.CarRepository;
import com.company.carrental.repository.ReservationRepository;
import com.company.carrental.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {

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
    }

    @Test
    void shouldCreateReservation() {
        // Setup test data
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);

        CarDTO carDTO = new CarDTO();
        carDTO.setCarId(1);

        User user = new User();
        user.setUserId(1);

        Car car = new Car();
        car.setCarId(1);
        car.setStatus(Car.CarStatus.RESERVED);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUser(userDTO);
        reservationDTO.setCar(carDTO);
        reservationDTO.setStartDate(LocalDate.now());
        reservationDTO.setEndDate(LocalDate.now().plusDays(2));

        // Mock repository responses
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> {
                    Reservation savedReservation = invocation.getArgument(0);
                    savedReservation.setReservationId(1);
                    return savedReservation;
                });
        when(userService.getUserById(1)).thenReturn(userDTO);
        when(carService.getCarById(1)).thenReturn(carDTO);

        // Execute test
        ReservationDTO created = reservationService.createReservation(reservationDTO);

        // Verify
        assertNotNull(created);
        assertEquals(Reservation.ReservationStatus.ACTIVE, created.getStatus());
        verify(carRepository).save(any(Car.class));
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void shouldGetAllReservations() {
        // Setup test data
        User user = createTestUser(1);
        Car car = createTestCar(1, Car.CarStatus.AVAILABLE);

        Reservation reservation1 = new Reservation();
        reservation1.setReservationId(1);
        reservation1.setUser(user);
        reservation1.setCar(car);
        reservation1.setStartDate(LocalDate.now());
        reservation1.setEndDate(LocalDate.now().plusDays(2));

        Reservation reservation2 = new Reservation();
        reservation2.setReservationId(2);
        reservation2.setUser(user);
        reservation2.setCar(car);
        reservation2.setStartDate(LocalDate.now());
        reservation2.setEndDate(LocalDate.now().plusDays(2));

        List<Reservation> mockReservations = Arrays.asList(reservation1, reservation2);

        // Mock repository responses
        when(reservationRepository.findAll()).thenReturn(mockReservations);
        when(userService.getUserById(anyInt())).thenReturn(new UserDTO());
        when(carService.getCarById(anyInt())).thenReturn(new CarDTO());

        // Execute test
        List<ReservationDTO> reservations = reservationService.getAllReservations();

        // Verify
        assertEquals(2, reservations.size());
        verify(reservationRepository).findAll();
    }

    @Test
    void shouldCancelReservation() {
        // Setup test data
        Car car = new Car();
        car.setStatus(Car.CarStatus.RESERVED);

        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setCar(car);
        reservation.setStatus(Reservation.ReservationStatus.ACTIVE);

        // Mock repository responses
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));

        // Execute test
        reservationService.cancelReservation(1);

        // Verify
        assertEquals(Reservation.ReservationStatus.CANCELLED, reservation.getStatus());
        assertEquals(Car.CarStatus.AVAILABLE, car.getStatus());
        verify(reservationRepository).save(reservation);
        verify(carRepository).save(car);
    }

    @Test
    void shouldGetUserReservations() {
        // Setup test data
        User user = createTestUser(1);
        Car car = createTestCar(1, Car.CarStatus.AVAILABLE);

        Reservation reservation1 = new Reservation();
        reservation1.setReservationId(1);
        reservation1.setUser(user);
        reservation1.setCar(car);
        reservation1.setStartDate(LocalDate.now());
        reservation1.setEndDate(LocalDate.now().plusDays(2));

        Reservation reservation2 = new Reservation();
        reservation2.setReservationId(2);
        reservation2.setUser(user);
        reservation2.setCar(car);
        reservation2.setStartDate(LocalDate.now());
        reservation2.setEndDate(LocalDate.now().plusDays(2));

        List<Reservation> userReservations = Arrays.asList(reservation1, reservation2);

        // Mock repository responses
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(reservationRepository.findByUser(user)).thenReturn(userReservations);
        when(userService.getUserById(anyInt())).thenReturn(new UserDTO());
        when(carService.getCarById(anyInt())).thenReturn(new CarDTO());

        // Execute test
        List<ReservationDTO> reservations = reservationService.getUserReservations(1);

        // Verify
        assertEquals(2, reservations.size());
        verify(reservationRepository).findByUser(user);
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
}
