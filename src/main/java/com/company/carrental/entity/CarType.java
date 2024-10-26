/**
 * Represents a type of car in the car rental system.
 * The `CarType` entity is used to store information about the different types of cars available for rent, such as the name of the car type.
 * This entity is used throughout the application to manage and track the different car types.
 */
package com.company.carrental.entity;

import jakarta.persistence.*;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "car_types")
@Component
@Data
@AllArgsConstructor

public class CarType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int carTypeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private VehicleType vehicleType;

    public enum VehicleType {
        SEDAN, SUV, VAN
    }

    public CarType() {
        // Default constructor
    }

    public CarType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    // Getters and setters
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
