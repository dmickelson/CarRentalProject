package com.company.carrental.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import com.company.carrental.entity.CarType.VehicleType;

@DataJpaTest
public class CarTypeTest {

    @Test
    void shouldHaveThreeValidCarTypes() {
        CarType sedanType = new CarType(VehicleType.SEDAN);
        CarType suvType = new CarType(VehicleType.SUV);
        CarType vanType = new CarType(VehicleType.VAN);

        assertEquals(VehicleType.SEDAN, sedanType.getVehicleType());
        assertEquals(VehicleType.SUV, suvType.getVehicleType());
        assertEquals(VehicleType.VAN, vanType.getVehicleType());
    }

    @Test
    void shouldNotAllowInvalidVehicleType() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Attempt to create a CarType with an invalid enum value
            CarType invalidType = new CarType();
            invalidType.setCarTypeId(4);
            invalidType.setVehicleType(VehicleType.valueOf("TRUCK"));
        });
    }

}
