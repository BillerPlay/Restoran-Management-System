package com.ironhack.restaurantmanagementsystem.repository;
import com.ironhack.restaurantmanagementsystem.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable,Long> {
    Optional<RestaurantTable>findByTableNumber(int tableNumber);
    List<RestaurantTable>findAllByIsAvailable(boolean status);
    List<RestaurantTable>findByCapacityGreaterThanEqual(int capacity);
}
