package com.ironhack.restaurantmanagementsystem.dto.request;

import com.ironhack.restaurantmanagementsystem.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class OrderUpdateRequest {

    @NotNull( message = "Order status is required")
    private OrderStatus status;

    public OrderUpdateRequest() {}

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

