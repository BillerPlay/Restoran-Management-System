package com.ironhack.restaurantmanagementsystem.mapper;

import com.ironhack.restaurantmanagementsystem.dto.response.CategoryResponse;
import com.ironhack.restaurantmanagementsystem.entity.Category;

import java.util.List;

public class CategoryMapper {

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }

    public static List<CategoryResponse> toResponseList(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }
}