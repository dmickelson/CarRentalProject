package com.company.carrental.factory;

import org.springframework.stereotype.Component;

import com.company.carrental.entity.CarType;
import com.company.carrental.entity.CarType.VehicleType;

@Component
public class SedanFactory implements VehicleTypeFactory {
    @Override
    public CarType createVehicle() {
        return new CarType(VehicleType.SEDAN);
    }
}
