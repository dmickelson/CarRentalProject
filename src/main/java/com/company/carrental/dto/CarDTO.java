package com.company.carrental.dto;

import com.company.carrental.entity.Car;
import com.company.carrental.entity.CarType.VechicleType;

public class CarDTO {
    private Long carId;
    private VechicleType vehicleType;
    private Car.CarStatus status;

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public VechicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VechicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Car.CarStatus getStatus() {
        return status;
    }

    public void setStatus(Car.CarStatus status) {
        this.status = status;
    }
}
