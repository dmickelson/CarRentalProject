package com.company.carrental.dto;

import com.company.carrental.entity.CarType.VechicleType;

public class CarTypeDTO {
    private Long carTypeId;
    private VechicleType vehicleType;

    public Long getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(Long carTypeId) {
        this.carTypeId = carTypeId;
    }

    public VechicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VechicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
