package com.company.carrental.service;

import com.company.carrental.entity.Car;
import com.company.carrental.entity.CarType;
import com.company.carrental.entity.CarType.VehicleType;
import com.company.carrental.repository.CarRepository;
import com.company.carrental.repository.CarTypeRepository;
import com.company.carrental.factory.SedanFactory;
import com.company.carrental.factory.SUVFactory;
import com.company.carrental.factory.VehicleTypeFactory;
import com.company.carrental.dto.CarDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarTypeRepository carTypeRepository;

    private CarService carService;
    private List<VehicleTypeFactory> factories;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize factories
        factories = Arrays.asList(
                new SedanFactory(),
                new SUVFactory());
        carService = new CarService(carRepository, carTypeRepository, factories);
    }

    @Test
    void shouldGetAvailableCarsByType() {
        // Use SedanFactory to create CarType
        CarType sedanType = new SedanFactory().createVehicle();
        when(carTypeRepository.findByVehicleType(VehicleType.SEDAN)).thenReturn(sedanType);

        List<Car> mockCars = Arrays.asList(
                new Car(sedanType, Car.CarStatus.AVAILABLE),
                new Car(sedanType, Car.CarStatus.AVAILABLE));

        when(carRepository.findByCarType(sedanType)).thenReturn(mockCars);

        List<CarDTO> availableCars = carService.getCarsByType(sedanType);

        assertEquals(2, availableCars.size());
        verify(carRepository).findByCarType(sedanType);
    }

    @Test
    void shouldFilterAvailableCarsByType() {
        // Use SedanFactory to create CarType
        CarType sedanType = new SedanFactory().createVehicle();
        when(carTypeRepository.findByVehicleType(VehicleType.SEDAN)).thenReturn(sedanType);

        List<Car> mockCars = Arrays.asList(
                new Car(sedanType, Car.CarStatus.AVAILABLE),
                new Car(sedanType, Car.CarStatus.AVAILABLE),
                new Car(sedanType, Car.CarStatus.RESERVED));

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

    @Test
    void shouldCreateCarWithFactory() {
        CarDTO carDTO = new CarDTO();
        carDTO.setVehicleType(VehicleType.SEDAN);
        carDTO.setStatus(Car.CarStatus.AVAILABLE);

        CarType sedanType = new SedanFactory().createVehicle();
        when(carTypeRepository.findByVehicleType(VehicleType.SEDAN)).thenReturn(sedanType);

        Car newCar = new Car();
        newCar.setCarId(1);
        newCar.setCarType(sedanType);
        newCar.setStatus(Car.CarStatus.AVAILABLE);

        when(carRepository.save(any(Car.class))).thenReturn(newCar);

        CarDTO createdCar = carService.createCar(carDTO);

        assertNotNull(createdCar);
        assertEquals(VehicleType.SEDAN, createdCar.getVehicleType());
        assertEquals(Car.CarStatus.AVAILABLE, createdCar.getStatus());
    }
}