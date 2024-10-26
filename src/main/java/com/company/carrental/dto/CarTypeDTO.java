package com.company.carrental.dto;

import com.company.carrental.entity.CarType.VehicleType;

public class CarTypeDTO {
    private int carTypeId;
    private VehicleType vehicleType;

    public int getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(int carTypeId) {
        this.carTypeId = carTypeId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
