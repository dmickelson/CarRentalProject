package com.company.carrental.repository;

import com.company.carrental.entity.CarType;
import com.company.carrental.entity.Car;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CarRepositoryTest {
    /*
     * @Autowired
     * private CarRepository carRepository;
     * 
     * @Autowired
     * private CarTypeRepository carTypeRepository;
     * 
     * @Test
     * void shouldMaintainCarInventoryLimits() {
     * // Save car types
     * CarType sedan = carTypeRepository.save(new CarType(null, "SEDAN"));
     * CarType suv = carTypeRepository.save(new CarType(null, "SUV"));
     * CarType van = carTypeRepository.save(new CarType(null, "VAN"));
     * 
     * // Verify all three types exist
     * assertEquals(3, carTypeRepository.count());
     * 
     * // Add cars up to limits
     * for (int i = 0; i < 5; i++) {
     * carRepository.save(new Car(null, sedan));
     * }
     * for (int i = 0; i < 3; i++) {
     * carRepository.save(new Car(null, suv));
     * }
     * for (int i = 0; i < 2; i++) {
     * carRepository.save(new Car(null, van));
     * }
     * 
     * // Verify counts
     * assertEquals(5, carRepository.countByCarType_Name("SEDAN"));
     * assertEquals(3, carRepository.countByCarType_Name("SUV"));
     * assertEquals(2, carRepository.countByCarType_Name("VAN"));
     * }
     */
}
