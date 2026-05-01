package com.ironhack.restaurantmanagementsystem;


import com.ironhack.restaurantmanagementsystem.dto.request.CategoryCreateRequest;
import com.ironhack.restaurantmanagementsystem.dto.response.CategoryResponse;
import com.ironhack.restaurantmanagementsystem.entity.Category;
import com.ironhack.restaurantmanagementsystem.exception.ConflictException;
import com.ironhack.restaurantmanagementsystem.exception.ResourceNotFoundException;
import com.ironhack.restaurantmanagementsystem.repository.CategoryRepository;
import com.ironhack.restaurantmanagementsystem.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Burgers");
        category.setId(1L);
    }

    @Test
    void create_Success() {
        CategoryCreateRequest request = new CategoryCreateRequest();
        request.setName("Burgers");

        when(categoryRepository.existsByName("Burgers")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponse response = categoryService.create(request);

        assertNotNull(response);
        assertEquals("Burgers", response.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void create_DuplicateName_ThrowsConflictException() {
        CategoryCreateRequest request = new CategoryCreateRequest();
        request.setName("Burgers");

        when(categoryRepository.existsByName("Burgers")).thenReturn(true);

        assertThrows(ConflictException.class, () -> categoryService.create(request));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getAll_ReturnsList() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryResponse> result = categoryService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Burgers", result.get(0).getName());
    }

    @Test
    void getById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponse response = categoryService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getById_NotFound_ThrowsException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getById(99L));
    }

    @Test
    void update_Success() {
        CategoryCreateRequest request = new CategoryCreateRequest();
        request.setName("Pizzas");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponse response = categoryService.update(1L, request);

        assertNotNull(response);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void delete_Success() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.delete(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NotFound_ThrowsException() {
        when(categoryRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(99L));
    }
}

