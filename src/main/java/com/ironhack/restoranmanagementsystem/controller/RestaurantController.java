package com.ironhack.restoranmanagementsystem.controller;
import com.ironhack.restoranmanagementsystem.dto.request.TableCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.TableResponse;
import com.ironhack.restoranmanagementsystem.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
    public final RestaurantService restaurantService;
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    @PostMapping
    public TableResponse createTable(@RequestBody @Valid TableCreateRequest request){
        return restaurantService.createTable(request);
    }
    @PutMapping("/{id}")
    public TableResponse updateTable(@PathVariable Long id, @RequestBody @Valid TableCreateRequest request){
        return restaurantService.updateTable(id, request);
    }
    @DeleteMapping("/{id}")
    public void deleteTable(@PathVariable Long id){
        restaurantService.deleteTable(id);}
    @GetMapping("/status")
    public List<TableResponse>findAllByAvailable(@RequestParam boolean status) {
        return restaurantService.findAllAvailables(status);
    }
    @GetMapping("/capacity")
    public List<TableResponse>getTablesByMinCapacity(@RequestParam int minCapacity){
        return restaurantService.getTablesByMinCapacity(minCapacity);
    }
    @GetMapping("/number/{tableNumber}")
    public TableResponse findByTableNumber(@PathVariable int tableNumber){
        return restaurantService.findTableNumber(tableNumber);
    }
}
