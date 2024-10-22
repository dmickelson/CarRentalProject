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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car_types")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CarType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carTypeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private VechicleType vehicleType;

    public enum VechicleType {
        SEDAN, SUV, VAN
    }

    // Getters and setters
    public Long getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(Long carTypeId) {
        this.carTypeId = carTypeId;
    }

    public VechicleType getVechicleType() {
        return vehicleType;
    }

    public void setVechicleType(VechicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
