package com.company.carrental.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import com.company.carrental.entity.Reservation.ReservationStatus;
import com.company.carrental.entity.Car.CarStatus;
import com.company.carrental.entity.CarType.VechicleType;

@DataJpaTest
public class ReservationTest {

    @Test
    void shouldCreateValidReservation() {
        User user = new User();
        CarType sedanType = new CarType(VechicleType.SEDAN);
        Car car = new Car(sedanType, CarStatus.AVAILABLE);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);

        Reservation reservation = new Reservation(user, car, startDate, endDate, ReservationStatus.ACTIVE);

        assertEquals(user, reservation.getUser());
        assertEquals(car, reservation.getCar());
        assertEquals(startDate, reservation.getStartDate());
        assertEquals(endDate, reservation.getEndDate());
        assertEquals(ReservationStatus.ACTIVE, reservation.getStatus());
    }

    @Test
    void shouldHandleAllReservationStatuses() {
        Reservation reservation = new Reservation();

        reservation.setStatus(ReservationStatus.ACTIVE);
        assertEquals(ReservationStatus.ACTIVE, reservation.getStatus());

        reservation.setStatus(ReservationStatus.CANCELLED);
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());

        reservation.setStatus(ReservationStatus.COMPLETED);
        assertEquals(ReservationStatus.COMPLETED, reservation.getStatus());
    }

    @Test
    void shouldNotAllowInvalidReservationStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            Reservation reservation = new Reservation();
            reservation.setStatus(ReservationStatus.valueOf("INVALID_STATUS"));
        });
    }

    @Test
    void shouldUpdateReservationDatesCorrectly() {
        Reservation reservation = new Reservation();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);
        LocalDate newEndDate = startDate.plusDays(5);

        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);

        assertEquals(startDate, reservation.getStartDate());
        assertEquals(endDate, reservation.getEndDate());

        reservation.setEndDate(newEndDate);
        assertEquals(newEndDate, reservation.getEndDate());
    }

    @Test
    void shouldUpdateCarAndUserCorrectly() {
        Reservation reservation = new Reservation();
        User user1 = new User();
        User user2 = new User();
        CarType sedanType = new CarType(VechicleType.SEDAN);
        Car car1 = new Car(sedanType, CarStatus.AVAILABLE);
        Car car2 = new Car(sedanType, CarStatus.AVAILABLE);

        reservation.setUser(user1);
        reservation.setCar(car1);

        assertEquals(user1, reservation.getUser());
        assertEquals(car1, reservation.getCar());

        reservation.setUser(user2);
        reservation.setCar(car2);

        assertEquals(user2, reservation.getUser());
        assertEquals(car2, reservation.getCar());
    }
}
