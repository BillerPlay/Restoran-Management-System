package com.ironhack.restaurantmanagementsystem;

import com.ironhack.restaurantmanagementsystem.dto.request.ProductCreateRequest;
import com.ironhack.restaurantmanagementsystem.dto.response.ProductResponse;
import com.ironhack.restaurantmanagementsystem.entity.Category;
import com.ironhack.restaurantmanagementsystem.entity.Product;
import com.ironhack.restaurantmanagementsystem.exception.ResourceNotFoundException;
import com.ironhack.restaurantmanagementsystem.repository.CategoryRepository;
import com.ironhack.restaurantmanagementsystem.repository.ProductRepository;
import com.ironhack.restaurantmanagementsystem.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category("Burgers");
        category.setId(1L);

        product = new Product("Burger", "Tasty burger", new BigDecimal("8.50"), true, category);
        product.setId(1L);
    }

    @Test
    void create_Success() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("Burger");
        request.setDescription("Tasty burger");
        request.setPrice(new BigDecimal("8.50"));
        request.setAvailable(true);
        request.setCategoryId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.create(request);

        assertNotNull(response);
        assertEquals("Burger", response.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void create_CategoryNotFound_ThrowsException() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setCategoryId(99L);

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.create(request));
    }

    @Test
    void getAll_ReturnsList() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponse> result = productService.getAll();

        assertEquals(1, result.size());
        assertEquals("Burger", result.get(0).getName());
    }

    @Test
    void getAvailable_ReturnsOnlyAvailable() {
        when(productRepository.findByAvailableTrue()).thenReturn(List.of(product));

        List<ProductResponse> result = productService.getAvailable();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getAvailable());
    }

    @Test
    void getByCategory_ReturnsList() {
        when(productRepository.findByCategoryId(1L)).thenReturn(List.of(product));

        List<ProductResponse> result = productService.getByCategory(1L);

        assertEquals(1, result.size());
    }

    @Test
    void getById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getById_NotFound_ThrowsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getById(99L));
    }

    @Test
    void update_Success() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("Updated Burger");
        request.setDescription("Even tastier");
        request.setPrice(new BigDecimal("9.00"));
        request.setAvailable(true);
        request.setCategoryId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.update(1L, request);

        assertNotNull(response);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void delete_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.delete(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NotFound_ThrowsException() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productService.delete(99L));
    }
}


