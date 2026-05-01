package com.ironhack.restaurantmanagementsystem.mapper;

import com.ironhack.restaurantmanagementsystem.dto.request.UserRequest;
import com.ironhack.restaurantmanagementsystem.dto.response.UserResponse;
import com.ironhack.restaurantmanagementsystem.dto.response.UserSummary;
import com.ironhack.restaurantmanagementsystem.entity.User;

import java.util.List;

public class UserMapper {

    public static User toEntity(UserRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        return user;
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }

    public static UserSummary toSummary(User user) {
        return new UserSummary(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }

    public static List<UserSummary> toSummaryList(List<User> users) {
        return users.stream()
                .map(UserMapper::toSummary)
                .toList();
    }
}

