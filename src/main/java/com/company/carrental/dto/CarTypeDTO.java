package com.company.carrental.dto;

import com.company.carrental.entity.CarType.VechicleType;

public class CarTypeDTO {
    private int carTypeId;
    private VechicleType vehicleType;

    public int getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(int carTypeId) {
        this.carTypeId = carTypeId;
    }

    public VechicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VechicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
