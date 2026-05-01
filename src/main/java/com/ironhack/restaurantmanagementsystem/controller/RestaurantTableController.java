package com.ironhack.restaurantmanagementsystem.controller;

import com.ironhack.restaurantmanagementsystem.dto.request.TableCreateRequest;
import com.ironhack.restaurantmanagementsystem.dto.response.TableResponse;
import com.ironhack.restaurantmanagementsystem.service.RestaurantTableService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantTableController {
    private final RestaurantTableService restaurantTableService;

    public RestaurantTableController(RestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TableResponse createTable(@RequestBody @Valid TableCreateRequest request) {
        return restaurantTableService.createTable(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TableResponse updateTable(@PathVariable Long id, @RequestBody @Valid TableCreateRequest request) {
        return restaurantTableService.updateTable(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        restaurantTableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status")
    public List<TableResponse> findAllByAvailable(@RequestParam boolean status) {
        return restaurantTableService.findAllAvailables(status);
    }

    @GetMapping("/capacity")
    public List<TableResponse> getTablesByMinCapacity(@RequestParam int minCapacity) {
        return restaurantTableService.getTablesByMinCapacity(minCapacity);
    }

    @GetMapping("/number/{tableNumber}")
    public TableResponse findByTableNumber(@PathVariable int tableNumber) {
        return restaurantTableService.findTableNumber(tableNumber);
    }
}
