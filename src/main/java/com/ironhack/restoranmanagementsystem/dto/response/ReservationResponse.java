package com.ironhack.restoranmanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.restoranmanagementsystem.enums.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationResponse {
    private Long id;
    @JsonProperty("reservation_time")
    private LocalDateTime reservationTime;
    @JsonProperty("guest_count")
    private int guestCount;
    private ReservationStatus status;
    @JsonProperty("created_at")
    private LocalDate createdAt;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("user_full_name")
    private String userFullName;
    @JsonProperty("table_id")
    private Long tableId;
    @JsonProperty("table_number")
    private int tableNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
}
