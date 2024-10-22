package com.company.carrental.dto;

import com.company.carrental.entity.Car;

public class CarDTO {
    private Long carId;
    private CarTypeDTO carType;
    private Car.CarStatus status;

    // Getters and setters
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public CarTypeDTO getCarType() {
        return carType;
    }

    public void setCarType(CarTypeDTO carType) {
        this.carType = carType;
    }

    public Car.CarStatus getStatus() {
        return status;
    }

    public void setStatus(Car.CarStatus status) {
        this.status = status;
    }
}
