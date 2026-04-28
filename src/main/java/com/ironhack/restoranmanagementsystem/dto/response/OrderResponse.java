package com.ironhack.restoranmanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.restoranmanagementsystem.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("total_price")
    private BigDecimal totalPrice;
    private OrderStatus status;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("user_full_name")
    private String userFullName;
    @JsonProperty("order_items")
    private List<OrderItemResponse> orderItems;

    public OrderResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public List<OrderItemResponse> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemResponse> orderItems) { this.orderItems = orderItems; }
}