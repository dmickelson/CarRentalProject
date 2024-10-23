package com.company.carrental.service;

import com.company.carrental.entity.Car;
import com.company.carrental.entity.CarType;
import com.company.carrental.entity.CarType.VechicleType;
import com.company.carrental.repository.CarRepository;
import com.company.carrental.repository.CarTypeRepository;
import com.company.carrental.dto.CarDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarTypeRepository carTypeRepository;

    private CarService carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        carService = new CarService(carRepository, carTypeRepository);
    }

    @Test
    void shouldGetAvailableCarsByType() {
        CarType sedanType = new CarType(1L, VechicleType.SEDAN);
        List<Car> mockCars = Arrays.asList(
                new Car(1L, sedanType, Car.CarStatus.AVAILABLE),
                new Car(2L, sedanType, Car.CarStatus.AVAILABLE));

        when(carRepository.findByCarType(sedanType)).thenReturn(mockCars);

        List<CarDTO> availableCars = carService.getCarsByType(sedanType);

        assertEquals(2, availableCars.size());
        verify(carRepository).findByCarType(sedanType);
    }

    @Test
    void shouldFilterAvailableCarsByType() {
        CarType sedanType = new CarType(1L, VechicleType.SEDAN);

        // Create mix of available and reserved cars
        List<Car> mockCars = Arrays.asList(
                new Car(1L, sedanType, Car.CarStatus.AVAILABLE),
                new Car(2L, sedanType, Car.CarStatus.AVAILABLE),
                new Car(3L, sedanType, Car.CarStatus.RESERVED));

        when(carRepository.findByCarType(sedanType)).thenReturn(mockCars);
        when(carRepository.findByCarTypeAndStatus(sedanType, Car.CarStatus.AVAILABLE))
                .thenReturn(mockCars.stream()
                        .filter(car -> car.getStatus() == Car.CarStatus.AVAILABLE)
                        .collect(Collectors.toList()));

        List<CarDTO> totalSedans = carService.getCarsByType(sedanType);

        assertEquals(3, totalSedans.size());

        List<CarDTO> availableSedans = carService.getAvailableCarsByType(sedanType);
        assertEquals(2, availableSedans.size());
    }

}
