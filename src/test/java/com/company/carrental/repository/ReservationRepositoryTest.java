package com.company.carrental.repository;

import com.company.carrental.entity.*;
import com.company.carrental.entity.CarType.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CarTypeRepository carTypeRepository;

    private User testUser;
    private Car testCar;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        carRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setUsername("johndoe");
        testUser.setPassword("password123");
        userRepository.save(testUser);

        // Create test car
        CarType sedanType = carTypeRepository.findByVehicleType(VehicleType.SEDAN);
        testCar = new Car(sedanType, Car.CarStatus.AVAILABLE);
        carRepository.save(testCar);
    }

    @Test
    void shouldFindReservationsByUser() {
        // Create test reservations
        Reservation reservation1 = new Reservation();
        reservation1.setUser(testUser);
        reservation1.setCar(testCar);
        reservation1.setStartDate(LocalDate.now());
        reservation1.setEndDate(LocalDate.now().plusDays(2));
        reservation1.setStatus(Reservation.ReservationStatus.ACTIVE);

        Reservation reservation2 = new Reservation();
        reservation2.setUser(testUser);
        reservation2.setCar(testCar);
        reservation2.setStartDate(LocalDate.now().plusDays(3));
        reservation2.setEndDate(LocalDate.now().plusDays(5));
        reservation2.setStatus(Reservation.ReservationStatus.ACTIVE);

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        List<Reservation> userReservations = reservationRepository.findByUser(testUser);
        assertEquals(2, userReservations.size());
        assertTrue(userReservations.stream().allMatch(res -> res.getUser().equals(testUser)));
    }

    @Test
    void shouldFindOverlappingReservations() {
        LocalDate baseTime = LocalDate.now();

        // Create an existing reservation
        Reservation existingReservation = new Reservation();
        existingReservation.setUser(testUser);
        existingReservation.setCar(testCar);
        existingReservation.setStartDate(baseTime);
        existingReservation.setEndDate(baseTime.plusDays(3));
        existingReservation.setStatus(Reservation.ReservationStatus.ACTIVE);
        reservationRepository.save(existingReservation);

        // Test overlapping period
        List<Reservation> overlappingReservations = reservationRepository
                .findByCarCarIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        testCar.getCarId(),
                        baseTime.plusDays(2),
                        baseTime.plusDays(1));

        assertEquals(1, overlappingReservations.size());
    }

    @Test
    void shouldNotFindNonOverlappingReservations() {
        LocalDate baseTime = LocalDate.now();

        // Create an existing reservation
        Reservation existingReservation = new Reservation();
        existingReservation.setUser(testUser);
        existingReservation.setCar(testCar);
        existingReservation.setStartDate(baseTime);
        existingReservation.setEndDate(baseTime.plusDays(2));
        existingReservation.setStatus(Reservation.ReservationStatus.ACTIVE);
        reservationRepository.save(existingReservation);

        // Test non-overlapping period
        List<Reservation> nonOverlappingReservations = reservationRepository
                .findByCarCarIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        testCar.getCarId(),
                        baseTime.plusDays(5),
                        baseTime.plusDays(4));

        assertEquals(0, nonOverlappingReservations.size());
    }
}
