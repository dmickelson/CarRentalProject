package com.company.carrental.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import com.company.carrental.entity.Car.CarStatus;
import com.company.carrental.entity.CarType.VechicleType;

@DataJpaTest
public class CarTest {

    @Test
    void shouldCreateValidCar() {
        CarType sedanType = new CarType(VechicleType.SEDAN);
        Car car = new Car();
        car.setCarType(sedanType);
        car.setStatus(CarStatus.AVAILABLE);

        assertEquals(CarStatus.AVAILABLE, car.getStatus());
        assertEquals(VechicleType.SEDAN, car.getCarType().getVechicleType());
    }

    @Test
    void shouldHandleAllCarStatuses() {
        Car car = new Car();

        car.setStatus(CarStatus.AVAILABLE);
        assertEquals(CarStatus.AVAILABLE, car.getStatus());

        car.setStatus(CarStatus.RESERVED);
        assertEquals(CarStatus.RESERVED, car.getStatus());

        car.setStatus(CarStatus.MAINTENANCE);
        assertEquals(CarStatus.MAINTENANCE, car.getStatus());
    }

    @Test
    void shouldNotAllowInvalidCarStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            Car car = new Car();
            car.setStatus(CarStatus.valueOf("INVALID_STATUS"));
        });
    }

    @Test
    void shouldUpdateCarTypeCorrectly() {
        Car car = new Car();
        CarType sedanType = new CarType(VechicleType.SEDAN);
        CarType suvType = new CarType(VechicleType.SUV);

        car.setCarType(sedanType);
        assertEquals(VechicleType.SEDAN, car.getCarType().getVechicleType());

        car.setCarType(suvType);
        assertEquals(VechicleType.SUV, car.getCarType().getVechicleType());
    }
}
