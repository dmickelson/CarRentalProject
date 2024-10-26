package com.company.carrental.factory;

import com.company.carrental.entity.CarType;
import com.company.carrental.entity.CarType.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarTypeFactoryTest {

    private List<VehicleTypeFactory> factories;

    @BeforeEach
    void setUp() {
        factories = Arrays.asList(
                new SedanFactory(),
                new SUVFactory());
    }

    @Test
    void shouldCreateValidSedan() {
        VehicleTypeFactory sedanFactory = new SedanFactory();
        CarType sedanType = sedanFactory.createVehicle();

        assertNotNull(sedanType);
        assertEquals(VehicleType.SEDAN, sedanType.getVehicleType());
    }

    @Test
    void shouldCreateValidSUV() {
        VehicleTypeFactory suvFactory = new SUVFactory();
        CarType suvType = suvFactory.createVehicle();

        assertNotNull(suvType);
        assertEquals(VehicleType.SUV, suvType.getVehicleType());
    }

    @Test
    void shouldCreateDifferentTypesFromFactories() {
        CarType sedan = factories.get(0).createVehicle();
        CarType suv = factories.get(1).createVehicle();

        assertNotEquals(sedan.getVehicleType(), suv.getVehicleType());
    }
}