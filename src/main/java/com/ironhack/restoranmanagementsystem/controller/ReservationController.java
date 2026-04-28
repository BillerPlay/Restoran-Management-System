package com.ironhack.restoranmanagementsystem.controller;

import com.ironhack.restoranmanagementsystem.dto.request.ReservationCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.request.ReservationUpdateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.enums.ReservationStatus;
import com.ironhack.restoranmanagementsystem.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ReservationResponse> createReservation(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody ReservationCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.createReservation(email, request));
    }

    @GetMapping
    public List<ReservationResponse> getAll() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public ReservationResponse getById(@PathVariable Long id) {
        return reservationService.getById(id);
    }

    @GetMapping("/user/{userId}")
    public List<ReservationResponse> getMyReservations(@PathVariable Long userId) {
        return reservationService.getMyReservations(userId);
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationResponse> confirmReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.confirmReservation(id));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<ReservationResponse> cancelReservation(
            @PathVariable Long id,
            @AuthenticationPrincipal String email,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return ResponseEntity.ok(reservationService.cancelReservation(id, email, isAdmin));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ReservationResponse update(
            @PathVariable Long id,
            @AuthenticationPrincipal String email,
            Authentication authentication,
            @Valid @RequestBody ReservationUpdateRequest request) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return reservationService.updateReservation(id, request, email, isAdmin);
    }

    @GetMapping("/status")
    public ResponseEntity<List<ReservationResponse>> getByStatus(@RequestParam ReservationStatus status) {
        return ResponseEntity.ok(reservationService.getReservationByStatus(status));
    }

    @GetMapping("/user/{userId}/ordered")
    public List<ReservationResponse> getByUserIdOrdered(@PathVariable Long userId) {
        return reservationService.getByUserIdOrderByTime(userId);
    }

    @GetMapping("/min_guests")
    public List<ReservationResponse> getByMinGuests(@RequestParam int count) {
        return reservationService.getByMinGuestCount(count);
    }

}
