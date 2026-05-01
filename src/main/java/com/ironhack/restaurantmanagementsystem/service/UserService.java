package com.ironhack.restaurantmanagementsystem.service;

import com.ironhack.restaurantmanagementsystem.dto.request.UserCreateRequest;
import com.ironhack.restaurantmanagementsystem.dto.request.UserUpdateRequest;
import com.ironhack.restaurantmanagementsystem.dto.response.OrderResponse;
import com.ironhack.restaurantmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restaurantmanagementsystem.dto.response.UserResponse;
import com.ironhack.restaurantmanagementsystem.dto.response.UserSummary;
import com.ironhack.restaurantmanagementsystem.entity.Order;
import com.ironhack.restaurantmanagementsystem.entity.Reservation;
import com.ironhack.restaurantmanagementsystem.entity.User;
import com.ironhack.restaurantmanagementsystem.enums.RoleName;
import com.ironhack.restaurantmanagementsystem.exception.ConflictException;
import com.ironhack.restaurantmanagementsystem.exception.ResourceNotFoundException;
import com.ironhack.restaurantmanagementsystem.mapper.OrderMapper;
import com.ironhack.restaurantmanagementsystem.mapper.ReservationMapper;
import com.ironhack.restaurantmanagementsystem.mapper.UserMapper;
import com.ironhack.restaurantmanagementsystem.repository.OrderRepository;
import com.ironhack.restaurantmanagementsystem.repository.ReservationRepository;
import com.ironhack.restaurantmanagementsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository,
                       ReservationRepository reservationRepository,
                       OrderRepository orderRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + email));

        return UserMapper.toResponse(user);
    }

    public List<ReservationResponse> getMyReservations(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Reservation> reservations = reservationRepository.findByUser(user);

        return ReservationMapper.toResponseList(reservations);
    }

    public List<OrderResponse> getMyOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Order> orders = orderRepository.findByUser(user);

        return OrderMapper.toResponseList(orders);
    }

    public List<UserSummary> getAllUsers(){
        List<User> users = userRepository.findAll();

        return UserMapper.toSummaryList(users);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User createUser(UserCreateRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(
                    "User with email " + request.getEmail() + " already exists"
            );
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        RoleName role = RoleName.CUSTOMER;
        if (request.getRole() != null) {
            role = RoleName.valueOf(request.getRole().toUpperCase());
        }

        user.setRole(role);

        return userRepository.save(user);
    }

    public User updateUser(Long id, UserUpdateRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getEmail().equalsIgnoreCase(request.getEmail())) {
            boolean emailExists = userRepository.existsByEmail(request.getEmail());
            if (emailExists) {
                throw new ConflictException("User with this email already exists: " + request.getEmail());
            }
        }
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        RoleName role = RoleName.CUSTOMER;
        if (request.getRole() != null) {
            role = RoleName.valueOf(request.getRole().toUpperCase());
        }
        user.setRole(role);

        return userRepository.save(user);
    }
}
