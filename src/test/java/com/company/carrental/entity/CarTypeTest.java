package com.company.carrental.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import com.company.carrental.entity.CarType.VechicleType;

@DataJpaTest
public class CarTypeTest {

    @Test
    void shouldHaveThreeValidCarTypes() {
        CarType sedanType = new CarType(VechicleType.SEDAN);
        CarType suvType = new CarType(VechicleType.SUV);
        CarType vanType = new CarType(VechicleType.VAN);

        assertEquals(VechicleType.SEDAN, sedanType.getVechicleType());
        assertEquals(VechicleType.SUV, suvType.getVechicleType());
        assertEquals(VechicleType.VAN, vanType.getVechicleType());
    }

    @Test
    void shouldNotAllowInvalidVehicleType() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Attempt to create a CarType with an invalid enum value
            CarType invalidType = new CarType();
            invalidType.setCarTypeId(4);
            invalidType.setVechicleType(VechicleType.valueOf("TRUCK"));
        });
    }

}
