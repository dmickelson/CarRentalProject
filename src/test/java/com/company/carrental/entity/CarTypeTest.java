package com.company.carrental.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.company.carrental.entity.CarType.VechicleType;

public class CarTypeTest {

    @Test
    void shouldHaveThreeValidCarTypes() {
        CarType sedan = new CarType(1L, VechicleType.SEDAN);
        CarType suv = new CarType(2L, VechicleType.SUV);
        CarType van = new CarType(3L, VechicleType.VAN);

        assertEquals(VechicleType.SEDAN, sedan.getVechicleType());
        assertEquals(VechicleType.SUV, suv.getVechicleType());
        assertEquals(VechicleType.VAN, van.getVechicleType());
    }
}
