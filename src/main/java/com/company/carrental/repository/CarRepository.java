package com.company.carrental.repository;

import com.company.carrental.entity.Car;
import com.company.carrental.entity.CarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByCarTypeAndStatus(CarType carType, Car.CarStatus status);

    List<Car> findByStatus(Car.CarStatus status);
}
