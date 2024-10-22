package com.company.carrental.repository;

import com.company.carrental.entity.CarType;
import com.company.carrental.entity.CarType.VechicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarTypeRepository extends JpaRepository<CarType, Long> {
    CarType findByVehicleType(VechicleType vehicleType);
}
