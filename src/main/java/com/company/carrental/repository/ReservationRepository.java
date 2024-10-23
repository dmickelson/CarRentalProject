package com.company.carrental.repository;

import com.company.carrental.entity.Reservation;
import com.company.carrental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUser(User user);

    List<Reservation> findByCarCarIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Integer carId, LocalDateTime endDate, LocalDateTime startDate);
}
