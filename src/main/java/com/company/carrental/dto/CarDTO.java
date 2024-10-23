package com.company.carrental.dto;

import com.company.carrental.entity.Car;
import com.company.carrental.entity.CarType.VechicleType;

public class CarDTO {
    private int carId;
    private VechicleType vehicleType;
    private Car.CarStatus status;

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
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
