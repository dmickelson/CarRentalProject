package com.company.carrental.repository;

import com.company.carrental.entity.CarType;
import com.company.carrental.entity.CarType.VechicleType;

import jakarta.persistence.criteria.CriteriaBuilder.In;

import com.company.carrental.entity.Car;

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

    @Test
    void shouldMaintainCarInventoryLimits() {
        // Save car types
        CarType sedanType = carTypeRepository.save(new CarType(1L, VechicleType.SEDAN));
        CarType suvType = carTypeRepository.save(new CarType(2L, VechicleType.SUV));
        CarType vanType = carTypeRepository.save(new CarType(3L, VechicleType.VAN));

        // Verify all three types exist
        assertEquals(3, carTypeRepository.count());

        // Add cars up to limits
        for (int i = 0; i < 5; i++) {
            Car car = new Car((long) i, sedanType, Car.CarStatus.AVAILABLE);
            carRepository.save(car);
        }
        for (int i = 0; i < 3; i++) {
            Car car = new Car((long) i, suvType, Car.CarStatus.AVAILABLE);
            carRepository.save(car);
        }
        for (int i = 0; i < 2; i++) {
            Car car = new Car((long) i, vanType, Car.CarStatus.AVAILABLE);
            carRepository.save(car);
        }

        // Verify counts
        assertEquals(5, carRepository.findByCarType(sedanType).size());
        assertEquals(3, carRepository.findByCarType(suvType).size());
        assertEquals(2, carRepository.findByCarType(vanType).size());
    }

    @Test
    void shouldFindAvailableCarsByTypeAndStatus() {
        // Save car types
        CarType sedanType = carTypeRepository.save(new CarType(1L, VechicleType.SEDAN));

        // Create mix of available and reserved sedans
        Car availableSedan1 = new Car(1L, sedanType, Car.CarStatus.AVAILABLE);
        Car availableSedan2 = new Car(2L, sedanType, Car.CarStatus.AVAILABLE);
        Car reservedSedan = new Car(3L, sedanType, Car.CarStatus.RESERVED);

        carRepository.saveAll(Arrays.asList(availableSedan1, availableSedan2, reservedSedan));

        // Test finding by type and status
        List<Car> availableSedans = carRepository.findByCarTypeAndStatus(sedanType, Car.CarStatus.AVAILABLE);
        List<Car> reservedSedans = carRepository.findByCarTypeAndStatus(sedanType, Car.CarStatus.RESERVED);

        // Verify results
        assertEquals(2, availableSedans.size());
        assertEquals(1, reservedSedans.size());
        assertTrue(availableSedans.stream().allMatch(car -> car.getStatus() == Car.CarStatus.AVAILABLE));
        assertTrue(reservedSedans.stream().allMatch(car -> car.getStatus() == Car.CarStatus.RESERVED));
    }

}
