package com.company.carrental.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import com.company.carrental.entity.Car.CarStatus;
import com.company.carrental.entity.CarType.VehicleType;

@DataJpaTest
public class CarTest {

    @Test
    void shouldCreateValidCar() {
        CarType sedanType = new CarType(VehicleType.SEDAN);
        Car car = new Car();
        car.setCarType(sedanType);
        car.setStatus(CarStatus.AVAILABLE);

        assertEquals(CarStatus.AVAILABLE, car.getStatus());
        assertEquals(VehicleType.SEDAN, car.getCarType().getVehicleType());
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
        CarType sedanType = new CarType(VehicleType.SEDAN);
        CarType suvType = new CarType(VehicleType.SUV);

        car.setCarType(sedanType);
        assertEquals(VehicleType.SEDAN, car.getCarType().getVehicleType());

        car.setCarType(suvType);
        assertEquals(VehicleType.SUV, car.getCarType().getVehicleType());
    }
}
