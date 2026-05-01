package com.ironhack.restaurantmanagementsystem;

import com.ironhack.restaurantmanagementsystem.dto.request.OrderCreateRequest;
import com.ironhack.restaurantmanagementsystem.dto.request.OrderItemAddRequest;
import com.ironhack.restaurantmanagementsystem.dto.response.OrderResponse;
import com.ironhack.restaurantmanagementsystem.dto.response.OrderSummary;
import com.ironhack.restaurantmanagementsystem.entity.Order;
import com.ironhack.restaurantmanagementsystem.entity.OrderItem;
import com.ironhack.restaurantmanagementsystem.entity.Product;
import com.ironhack.restaurantmanagementsystem.entity.User;
import com.ironhack.restaurantmanagementsystem.enums.OrderStatus;
import com.ironhack.restaurantmanagementsystem.exception.BadRequestException;
import com.ironhack.restaurantmanagementsystem.exception.ForbiddenException;
import com.ironhack.restaurantmanagementsystem.exception.ResourceNotFoundException;
import com.ironhack.restaurantmanagementsystem.repository.OrderItemRepository;
import com.ironhack.restaurantmanagementsystem.repository.OrderRepository;
import com.ironhack.restaurantmanagementsystem.repository.ProductRepository;
import com.ironhack.restaurantmanagementsystem.repository.UserRepository;
import com.ironhack.restaurantmanagementsystem.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;



    private User user;
    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setFullName("Test User");

        product = new Product();
        product.setId(1L);
        product.setName("Pizza");
        product.setPrice(new BigDecimal("10.00"));
        product.setAvailable(true);

        order = new Order(LocalDateTime.now(), BigDecimal.ZERO, OrderStatus.PENDING, user);
        order.setId(1L);
        order.setOrderItems(new ArrayList<>());
    }

    @Test
    void createOrder_Success() {
        OrderItemAddRequest itemRequest = new OrderItemAddRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        OrderCreateRequest request = new OrderCreateRequest();
        request.setItems(List.of(itemRequest));

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());
        when(orderItemRepository.findByOrderId(1L)).thenReturn(List.of());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.createOrder(request, "test@test.com");

        assertNotNull(response);
        verify(orderRepository, atLeastOnce()).save(any(Order.class));
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void createOrder_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("wrong@test.com")).thenReturn(Optional.empty());

        OrderCreateRequest request = new OrderCreateRequest();
        request.setItems(List.of());

        assertThrows(ResourceNotFoundException.class, () ->
                orderService.createOrder(request, "wrong@test.com"));
    }

    @Test
    void createOrder_ProductNotAvailable_ThrowsException() {
        product.setAvailable(false);

        OrderItemAddRequest itemRequest = new OrderItemAddRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(1);

        OrderCreateRequest request = new OrderCreateRequest();
        request.setItems(List.of(itemRequest));

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(BadRequestException.class, () ->
                orderService.createOrder(request, "test@test.com"));
    }

    @Test
    void shouldAddItemToOrderSuccessfully() {
        OrderItemAddRequest request = new OrderItemAddRequest();
        request.setProductId(1L);
        request.setQuantity(1);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());
        when(orderItemRepository.findByOrderId(1L)).thenReturn(List.of());
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.addItem(1L, request, "test@test.com");

        assertNotNull(response);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }
    @Test
    void addItem_WrongUser_ThrowsForbiddenException() {
        OrderItemAddRequest request = new OrderItemAddRequest();
        request.setProductId(1L);
        request.setQuantity(1);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ForbiddenException.class, () ->
                orderService.addItem(1L, request, "other@test.com"));
    }

    @Test
    void addItem_OrderNotPending_ThrowsBadRequestException() {
        order.setStatus(OrderStatus.COMPLETED);

        OrderItemAddRequest request = new OrderItemAddRequest();
        request.setProductId(1L);
        request.setQuantity(1);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () ->
                orderService.addItem(1L, request, "test@test.com"));
    }

    @Test
    void addItem_ProductNotFound_ThrowsException() {
        OrderItemAddRequest request = new OrderItemAddRequest();
        request.setProductId(99L);
        request.setQuantity(1);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                orderService.addItem(1L, request, "test@test.com"));
    }

    @Test
    void updateStatus_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.updateStatus(1L, OrderStatus.PREPARING);

        assertNotNull(response);
        assertEquals(OrderStatus.PREPARING, order.getStatus());
    }

    @Test
    void updateStatus_OrderNotFound_ThrowsException() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                orderService.updateStatus(99L, OrderStatus.COMPLETED));
    }

    @Test
    void getAll_ReturnsSummaryList() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderSummary> result = orderService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.getById(1L, "test@test.com", false);

        assertNotNull(response);
    }

    @Test
    void getById_WrongUser_ThrowsForbiddenException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ForbiddenException.class, () ->
                orderService.getById(1L, "other@test.com", false));
    }

    @Test
    void getById_Admin_CanAccessAnyOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.getById(1L, "admin@test.com", true);

        assertNotNull(response);
    }
}

