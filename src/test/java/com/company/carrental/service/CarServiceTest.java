package com.company.carrental.service;

import com.company.carrental.repository.CarRepository;
import com.company.carrental.repository.CarTypeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CarServiceTest {

    @Mock
    private CarRepository carRepository;
    private CarTypeRepository carTypeRepository;

    private CarService carService;

    @BeforeEach
    void setUp() {
        carService = new CarService(carRepository, carTypeRepository);
    }

    @Test
     * void shouldEnforceCarTypeLimits() {
     * // Setup test data
     * int sedanLimit = 5;
     * int suvLimit = 3;
     * int vanLimit = 2;
     * 
     * when(carRepository.countByCarType_Name("SEDAN")).thenReturn(sedanLimit);
     * when(carRepository.countByCarType_Name("SUV")).thenReturn(suvLimit);
     * when(carRepository.countByCarType_Name("VAN")).thenReturn(vanLimit);
     * 
     * // Verify limits
     * assertThrows(CarLimitExceededException.class, () -> {
     * carService.addCar(new Car(null, new CarType(1L, "SEDAN")));
     * });
     * 
     * verify(carRepository).countByCarType_Name("SEDAN");
     * }**

    @Test
     * void shouldGetAvailableCarsByType() {
     * String carType = "SEDAN";
     * when(carRepository.findAvailableCarsByType(carType)).thenReturn(Arrays.
     * asList(
     * new Car(1L, new CarType(1L, "SEDAN")),
     * new Car(2L, new CarType(1L, "SEDAN"))));
     * 
     * List<Car> availableCars = carService.getAvailableCarsByType(carType);
     * 
     * assertEquals(2, availableCars.size());
     * verify(carRepository).findAvailableCarsByType(carType);
     * }*/
}
