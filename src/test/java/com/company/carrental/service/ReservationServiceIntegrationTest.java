/*
#ReservationServiceIntegrationTest Documentation

## Overview 
Integration test suite for the Reservation Service in the Car Rental Project.Tests the complete reservation 
workflow with real repository interactions.

## Key Test Cases

1.`shouldSuccessfullyCreateMultipleReservationsAcrossMonths`
  -Validates creation of non-overlapping reservations across different months
  -Tests reservation creation for October and November 2024

2.`shouldFailWhenCreatingOverlappingReservations`
  -Verifies system prevents double-booking of the same car
  -Tests overlapping date ranges throw appropriate exceptions

3.`shouldHandleMultipleReservationsForDifferentCars`
  -Validates simultaneous bookings for different vehicle types
  -Ensures distinct cars can be reserved for the same time period

4.`shouldFailWhenAllCarsOfDifferentTypesAreReservedForRequestedPeriod`
  -Tests system behavior when all vehicles are booked
  -Verifies proper handling of fully-booked scenarios

##Test Setup
  -Uses`@SpringBootTest`for full application context
  -Implements`@BeforeEach`to reset test data
  -Creates test data for:
    -Car Types(SEDAN,SUV,VAN)
    -Users
    -Cars
    -Reservations

## Helper Methods
  -`createTestUserDTO`:Creates user data transfer objects
  -`createTestCarDTO`:Creates car data transfer objects
  -`createTestCar`:Creates car entities
  -`createTestUser`:Creates user entities
  -`createReservationDTO`:Creates reservation data transfer objects

*/

package com.company.carrental.service;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.company.carrental.repository.CarTypeRepository;
import com.company.carrental.repository.ReservationRepository;
import com.company.carrental.repository.UserRepository;

@SpringBootTest
public class ReservationServiceIntegrationTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CarTypeRepository carTypeRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        // Clear existing data
        reservationRepository.deleteAll();
        carRepository.deleteAll();
        userRepository.deleteAll();
        carTypeRepository.deleteAll();

        // Create and persist car types
        CarType sedanType = new CarType(VechicleType.SEDAN);
        CarType suvType = new CarType(VechicleType.SUV);
        CarType vanType = new CarType(VechicleType.VAN);

        carTypeRepository.save(sedanType);
        carTypeRepository.save(suvType);
        carTypeRepository.save(vanType);

        // Initialize the service with real repositories
        reservationService = new ReservationService(
                reservationRepository,
                carRepository,
                userRepository,
                carService,
                userService);
    }

    @Test
    void shouldSuccessfullyCreateMultipleReservationsAcrossMonths() {
        // Create and persist a test user
        User user = createTestUser(1);
        User savedUser = userRepository.save(user);

        // Create and persist a test car
        CarType sedanType = carTypeRepository.findByVehicleType(VechicleType.SEDAN);
        Car car = createTestCar(1, sedanType.getVehicleType(), Car.CarStatus.AVAILABLE);
        Car savedCar = carRepository.save(car);

        // Create reservation DTOs
        UserDTO userDTO = createTestUserDTO(savedUser.getUserId());
        CarDTO carDTO = createTestCarDTO(savedCar.getCarId(), savedCar.getCarType().getVehicleType(),
                savedCar.getStatus());

        ReservationDTO octReservation = createReservationDTO(
                LocalDate.of(2024, 10, 1),
                LocalDate.of(2024, 10, 3),
                carDTO,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        ReservationDTO novReservation = createReservationDTO(
                LocalDate.of(2024, 11, 1),
                LocalDate.of(2024, 11, 3),
                carDTO,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

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
        // Create and persist test user
        User user = createTestUser(1);
        User savedUser = userRepository.save(user);

        // Create and persist test car
        CarType sedanType = carTypeRepository.findByVehicleType(VechicleType.SEDAN);
        Car car = createTestCar(1, sedanType.getVehicleType(), Car.CarStatus.AVAILABLE);
        Car savedCar = carRepository.save(car);

        // Create DTOs using saved entities' IDs
        UserDTO userDTO = createTestUserDTO(savedUser.getUserId());
        CarDTO carDTO = createTestCarDTO(savedCar.getCarId(), savedCar.getCarType().getVehicleType(),
                savedCar.getStatus());

        // Create overlapping reservations
        ReservationDTO firstReservation = createReservationDTO(
                LocalDate.of(2024, 10, 1),
                LocalDate.of(2024, 10, 5),
                carDTO,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        ReservationDTO overlappingReservation = createReservationDTO(
                LocalDate.of(2024, 10, 3),
                LocalDate.of(2024, 10, 7),
                carDTO,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        // Create first reservation
        reservationService.createReservation(firstReservation);

        // Verify that creating overlapping reservation throws exception
        assertThrows(RuntimeException.class, () -> {
            reservationService.createReservation(overlappingReservation);
        });
    }

    @Test
    void shouldHandleMultipleReservationsForDifferentCars() {
        // Create and persist test user
        User user = createTestUser(1);
        User savedUser = userRepository.save(user);

        // Create and persist two different cars
        CarType sedanType = carTypeRepository.findByVehicleType(VechicleType.SEDAN);
        CarType vanType = carTypeRepository.findByVehicleType(VechicleType.VAN);

        Car car1 = createTestCar(1, sedanType.getVehicleType(), Car.CarStatus.AVAILABLE);
        Car car2 = createTestCar(2, vanType.getVehicleType(), Car.CarStatus.AVAILABLE);

        Car savedCar1 = carRepository.save(car1);
        Car savedCar2 = carRepository.save(car2);

        // Create DTOs using saved entities' IDs
        UserDTO userDTO = createTestUserDTO(savedUser.getUserId());
        CarDTO carDTO1 = createTestCarDTO(savedCar1.getCarId(), savedCar1.getCarType().getVehicleType(),
                savedCar1.getStatus());
        CarDTO carDTO2 = createTestCarDTO(savedCar2.getCarId(), savedCar2.getCarType().getVehicleType(),
                savedCar2.getStatus());

        // Create simultaneous reservations for different cars
        ReservationDTO reservation1 = createReservationDTO(
                LocalDate.of(2024, 10, 1),
                LocalDate.of(2024, 10, 3),
                carDTO1,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        ReservationDTO reservation2 = createReservationDTO(
                LocalDate.of(2024, 10, 1),
                LocalDate.of(2024, 10, 3),
                carDTO2,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        // Execute and verify
        ReservationDTO created1 = reservationService.createReservation(reservation1);
        ReservationDTO created2 = reservationService.createReservation(reservation2);

        assertNotNull(created1);
        assertNotNull(created2);
        assertNotEquals(created1.getCar().getCarId(), created2.getCar().getCarId());
    }

    @Test
    void shouldFailWhenAllCarsOfDifferentTypesAreReservedForRequestedPeriod() {
        // Create and persist test user
        User user = createTestUser(1);
        User savedUser = userRepository.save(user);

        // Create and persist cars of different types
        CarType sedanType = carTypeRepository.findByVehicleType(VechicleType.SEDAN);
        CarType suvType = carTypeRepository.findByVehicleType(VechicleType.SUV);
        CarType vanType = carTypeRepository.findByVehicleType(VechicleType.VAN);

        Car sedan = createTestCar(1, sedanType.getVehicleType(), Car.CarStatus.AVAILABLE);
        Car suv = createTestCar(2, suvType.getVehicleType(), Car.CarStatus.AVAILABLE);
        Car van = createTestCar(3, vanType.getVehicleType(), Car.CarStatus.AVAILABLE);

        Car savedSedan = carRepository.save(sedan);
        Car savedSuv = carRepository.save(suv);
        Car savedVan = carRepository.save(van);

        // Create DTOs using saved entities' IDs
        UserDTO userDTO = createTestUserDTO(savedUser.getUserId());
        CarDTO sedanDTO = createTestCarDTO(savedSedan.getCarId(), savedSedan.getCarType().getVehicleType(),
                savedSedan.getStatus());
        CarDTO suvDTO = createTestCarDTO(savedSuv.getCarId(), savedSuv.getCarType().getVehicleType(),
                savedSuv.getStatus());
        CarDTO vanDTO = createTestCarDTO(savedVan.getCarId(), savedVan.getCarType().getVehicleType(),
                savedVan.getStatus());

        // Create reservations for all cars
        ReservationDTO sedanReservation = createReservationDTO(
                LocalDate.of(2024, 12, 1),
                LocalDate.of(2024, 12, 5),
                sedanDTO,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        ReservationDTO suvReservation = createReservationDTO(
                LocalDate.of(2024, 12, 2),
                LocalDate.of(2024, 12, 6),
                suvDTO,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        ReservationDTO vanReservation = createReservationDTO(
                LocalDate.of(2024, 12, 3),
                LocalDate.of(2024, 12, 7),
                vanDTO,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        // Create all initial reservations
        reservationService.createReservation(sedanReservation);
        reservationService.createReservation(suvReservation);
        reservationService.createReservation(vanReservation);

        // Attempt to create overlapping reservation
        ReservationDTO newReservation = createReservationDTO(
                LocalDate.of(2024, 12, 3),
                LocalDate.of(2024, 12, 9),
                suvDTO,
                userDTO,
                Reservation.ReservationStatus.ACTIVE);

        // Verify that creating a new reservation fails
        assertThrows(RuntimeException.class, () -> {
            reservationService.createReservation(newReservation);
        });
    }

    private UserDTO createTestUserDTO(Integer userId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setFirstName("Test" + userId);
        userDTO.setLastName("User" + userId);
        return userDTO;
    }

    private CarDTO createTestCarDTO(Integer carId, VechicleType vehicleType, Car.CarStatus status) {
        CarDTO carDTO = new CarDTO();
        carDTO.setCarId(carId);
        carDTO.setStatus(status);
        carDTO.setVehicleType(vehicleType);
        return carDTO;
    }

    private Car createTestCar(Integer carId, VechicleType vehicleType, Car.CarStatus status) {
        Car car = new Car();
        car.setCarId(carId);
        car.setStatus(status);
        car.setCarType(carTypeRepository.findByVehicleType(vehicleType));
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
