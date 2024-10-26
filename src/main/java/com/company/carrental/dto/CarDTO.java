package com.company.carrental.dto;

import com.company.carrental.entity.Car;
import com.company.carrental.entity.CarType.VehicleType;

public class CarDTO {
    private int carId;
    private VehicleType vehicleType;
    private Car.CarStatus status;

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Car.CarStatus getStatus() {
        return status;
    }

    public void setStatus(Car.CarStatus status) {
        this.status = status;
    }
}
