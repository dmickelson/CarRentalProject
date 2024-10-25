package com.company.carrental.service;

import com.company.carrental.dto.CarDTO;
import com.company.carrental.entity.Car;
import com.company.carrental.entity.CarType;
import com.company.carrental.entity.CarType.VechicleType;
import com.company.carrental.factory.*;
import com.company.carrental.repository.CarRepository;
import com.company.carrental.repository.CarTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarTypeRepository carTypeRepository;
    private final Map<VechicleType, VehicleTypeFactory> vehicleFactories;

    @Autowired
    public CarService(CarRepository carRepository,
            CarTypeRepository carTypeRepository,
            List<VehicleTypeFactory> factories) {
        this.carRepository = carRepository;
        this.carTypeRepository = carTypeRepository;
        this.vehicleFactories = factories.stream()
                .collect(Collectors.toMap(
                        factory -> factory.createVehicle().getVehicleType(),
                        factory -> factory));
    }

    public List<CarDTO> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CarDTO> getCarsByType(CarType carType) {
        return carRepository.findByCarType(carType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CarDTO getCarById(int id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));
        return convertToDTO(car);
    }

    public List<CarDTO> getAvailableCars() {
        return carRepository.findByStatus(Car.CarStatus.AVAILABLE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CarDTO> getAvailableCarsByType(CarType carType) {
        return carRepository.findByCarTypeAndStatus(
                carType, Car.CarStatus.AVAILABLE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }

    @Transactional
    public CarDTO createCar(CarDTO carDTO) {
        Car car = new Car();
        updateCarFromDTO(car, carDTO);
        Car savedCar = carRepository.save(car);
        return convertToDTO(savedCar);
    }

    @Transactional
    public void deleteCar(int id) {
        carRepository.deleteById(id);
    }

    @Transactional
    public CarDTO updateCar(int id, CarDTO carDTO) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));
        updateCarFromDTO(car, carDTO);
        Car updatedCar = carRepository.save(car);
        return convertToDTO(updatedCar);
    }

    public CarDTO convertToDTO(Car car) {
        CarDTO dto = new CarDTO();
        dto.setCarId(car.getCarId());
        dto.setStatus(car.getStatus());
        if (car.getCarType() != null) {
            dto.setVehicleType(car.getCarType().getVehicleType());
        }
        return dto;
    }

    private void updateCarFromDTO(Car car, CarDTO carDTO) {
        car.setStatus(carDTO.getStatus());
        if (carDTO.getVehicleType() != null) {
            CarType carType = carTypeRepository.findByVehicleType(carDTO.getVehicleType());
            if (carType == null) {
                VehicleTypeFactory factory = vehicleFactories.get(carDTO.getVehicleType());
                carType = factory.createVehicle();
                carType = carTypeRepository.save(carType);
            }
            car.setCarType(carType);
        }
    }
}
