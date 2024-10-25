package com.company.carrental.controller;

import com.company.carrental.dto.CarTypeDTO;
import com.company.carrental.service.CarTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartypes")
public class CarTypeController {
    private final CarTypeService carTypeService;

    @Autowired
    public CarTypeController(CarTypeService carTypeService) {
        this.carTypeService = carTypeService;
    }

    @GetMapping
    public List<CarTypeDTO> getAllCarTypes() {
        return carTypeService.getAllCarTypes();
    }

    @GetMapping("/{id}")
    public CarTypeDTO getCarTypeById(@PathVariable int id) {
        return carTypeService.getCarTypeById(id);
    }
}