package com.company.carrental.repository;

import com.company.carrental.entity.CarType;
import com.company.carrental.entity.CarType.VehicleType;

import com.company.carrental.entity.Car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CarTypeRepository carTypeRepository;

    @BeforeEach
    void setUp() {
        // Clear existing data and reset sequence
        carRepository.deleteAll();

    }

    @Test
    void shouldMaintainCarInventoryLimits() {
        // Get existing car types from data.sql
        CarType sedanType = carTypeRepository.findByVehicleType(VehicleType.SEDAN);
        CarType suvType = carTypeRepository.findByVehicleType(VehicleType.SUV);
        CarType vanType = carTypeRepository.findByVehicleType(VehicleType.VAN);

        // Create cars
        int expectedSedanCount = 2;
        for (int i = 0; i < expectedSedanCount; i++) {
            Car car = new Car(sedanType, Car.CarStatus.AVAILABLE);
            carRepository.save(car);
        }

        int expectedSUVCount = 3;
        for (int i = 0; i < expectedSUVCount; i++) {
            Car car = new Car(suvType, Car.CarStatus.AVAILABLE);
            carRepository.save(car);
        }

        int expectedVANCount = 5;
        for (int i = 0; i < expectedVANCount; i++) {
            Car car = new Car(vanType, Car.CarStatus.AVAILABLE);
            carRepository.save(car);
        }

        // Verify counts
        assertEquals(expectedSedanCount, carRepository.findByCarType(sedanType).size());
        assertEquals(expectedSUVCount, carRepository.findByCarType(suvType).size());
        assertEquals(expectedVANCount, carRepository.findByCarType(vanType).size());
    }

    @Test
    void shouldFindAvailableCarsByTypeAndStatus() {

        // Clear existing data first
        carRepository.deleteAll();

        CarType sedanType = carTypeRepository.findByVehicleType(VehicleType.SEDAN);

        Car availableSedan1 = new Car(sedanType, Car.CarStatus.AVAILABLE);
        Car availableSedan2 = new Car(sedanType, Car.CarStatus.AVAILABLE);

        Car reservedSedan = new Car(sedanType, Car.CarStatus.RESERVED);

        carRepository.saveAll(Arrays.asList(availableSedan1, availableSedan2, reservedSedan));

        List<Car> availableSedans = carRepository.findByCarTypeAndStatus(sedanType, Car.CarStatus.AVAILABLE);
        List<Car> reservedSedans = carRepository.findByCarTypeAndStatus(sedanType, Car.CarStatus.RESERVED);

        assertEquals(2, availableSedans.size());
        assertEquals(1, reservedSedans.size());
        assertTrue(availableSedans.stream().allMatch(car -> car.getStatus() == Car.CarStatus.AVAILABLE));
        assertTrue(reservedSedans.stream().allMatch(car -> car.getStatus() == Car.CarStatus.RESERVED));

    }
}
