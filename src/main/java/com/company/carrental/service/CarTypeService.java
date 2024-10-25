package com.company.carrental.service;

import com.company.carrental.dto.CarTypeDTO;
import com.company.carrental.entity.CarType;
import com.company.carrental.repository.CarTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarTypeService {
    private final CarTypeRepository carTypeRepository;

    @Autowired
    public CarTypeService(CarTypeRepository carTypeRepository) {
        this.carTypeRepository = carTypeRepository;
    }

    public List<CarTypeDTO> getAllCarTypes() {
        return carTypeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CarTypeDTO getCarTypeById(int id) {
        CarType carType = carTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CarType not found"));
        return convertToDTO(carType);
    }

    private CarTypeDTO convertToDTO(CarType carType) {
        CarTypeDTO dto = new CarTypeDTO();
        dto.setCarTypeId(carType.getCarTypeId());
        dto.setVehicleType(carType.getVehicleType());
        return dto;
    }
}