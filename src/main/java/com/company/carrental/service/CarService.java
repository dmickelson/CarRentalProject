package com.company.carrental.service;

import com.company.carrental.dto.CarDTO;
import com.company.carrental.entity.Car;
import com.company.carrental.entity.CarType;
import com.company.carrental.repository.CarRepository;
import com.company.carrental.repository.CarTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarTypeRepository carTypeRepository;

    @Autowired
    public CarService(CarRepository carRepository, CarTypeRepository carTypeRepository) {
        this.carRepository = carRepository;
        this.carTypeRepository = carTypeRepository;
    }

    public List<CarDTO> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CarDTO getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));
        return convertToDTO(car);
    }

    public List<CarDTO> getAvailableCars() {
        return carRepository.findByStatus(Car.CarStatus.AVAILABLE).stream()
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
    public CarDTO updateCar(Long id, CarDTO carDTO) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));
        updateCarFromDTO(car, carDTO);
        Car updatedCar = carRepository.save(car);
        return convertToDTO(updatedCar);
    }

    @Transactional
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    private CarDTO convertToDTO(Car car) {
        CarDTO dto = new CarDTO();
        dto.setCarId(car.getCarId());
        dto.setStatus(car.getStatus());
        // Set CarType DTO
        return dto;
    }

    private void updateCarFromDTO(Car car, CarDTO carDTO) {
        car.setStatus(carDTO.getStatus());
        if (carDTO.getCarType() != null) {
            CarType carType = carTypeRepository.findById(carDTO.getCarType().getCarTypeId())
                    .orElseThrow(() -> new RuntimeException("CarType not found"));
            car.setCarType(carType);
        }
    }
}
