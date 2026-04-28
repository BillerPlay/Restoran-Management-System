package com.ironhack.restoranmanagementsystem.repository;
import com.ironhack.restoranmanagementsystem.entity.Reservation;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findByUserIdOrderByReservationTimeDesc(Long userId);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByGuestCountGreaterThanEqual(int count);
    List<Reservation> findByUser(User user);

    List<Reservation> findByUserEmailOrderByReservationTimeDesc(String email);

    boolean existsByRestaurantTableIdAndReservationTimeAndStatusNot(
            Long tableId,
            LocalDateTime reservationTime,
            ReservationStatus status
    );

    boolean existsByRestaurantTableIdAndReservationTimeAndStatusNotAndIdNot(
            Long tableId,
            LocalDateTime reservationTime,
            ReservationStatus status,
            Long id
    );

}
