package com.bookstore.backend.service;

import com.bookstore.backend.dto.request.OrderItemRequest;
import com.bookstore.backend.dto.request.OrderRequest;
import com.bookstore.backend.dto.response.OrderResponse;
import com.bookstore.backend.entity.Book;
import com.bookstore.backend.entity.Order;
import com.bookstore.backend.entity.OrderItem;
import com.bookstore.backend.entity.OrderStatus;
import com.bookstore.backend.entity.User;
import com.bookstore.backend.repository.BookRepository;
import com.bookstore.backend.repository.OrderRepository;
import com.bookstore.backend.repository.UserRepository;
import com.bookstore.backend.service.impl.OrderServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User testUser;
    private Book testBook;
    private Order testOrder;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setPrice(new BigDecimal("19.99"));
        testBook.setStock(10);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNumber("ORD-123456");
        testOrder.setUser(testUser);
        testOrder.setTotalAmount(new BigDecimal("19.99"));
        testOrder.setStatus(OrderStatus.PENDING);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setOrder(testOrder);
        orderItem.setBook(testBook);
        orderItem.setQuantity(1);
        orderItem.setPrice(testBook.getPrice());
        orderItem.setSubtotal(testBook.getPrice());

        testOrder.setItems(Arrays.asList(orderItem));

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setBookId(1L);
        itemRequest.setQuantity(1);

        orderRequest = new OrderRequest();
        orderRequest.setItems(Arrays.asList(itemRequest));
    }

    @Test
    void createOrder_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderResponse response = orderService.createOrder(1L, orderRequest);

        assertNotNull(response);
        assertEquals(testOrder.getOrderNumber(), response.getOrderNumber());
        assertEquals(testOrder.getTotalAmount(), response.getTotalAmount());
        verify(userRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(1L, orderRequest));
        verify(userRepository, times(1)).findById(1L);
        verify(bookRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_BookNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(1L, orderRequest));
        verify(userRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        OrderResponse response = orderService.getOrder(1L);

        assertNotNull(response);
        assertEquals(testOrder.getOrderNumber(), response.getOrderNumber());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void getOrder_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getOrder(1L));
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void getUserOrders_Success() {
        List<Order> orders = Arrays.asList(testOrder);
        Page<Order> orderPage = new PageImpl<>(orders);
        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findByUserId(1L, pageable)).thenReturn(orderPage);

        Page<OrderResponse> response = orderService.getUserOrders(1L, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        verify(orderRepository, times(1)).findByUserId(1L, pageable);
    }

    @Test
    void updateOrderStatus_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderResponse response = orderService.updateOrderStatus(1L, OrderStatus.COMPLETED.name());

        assertNotNull(response);
        assertEquals(OrderStatus.COMPLETED.name(), response.getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void updateOrderStatus_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.updateOrderStatus(1L, OrderStatus.COMPLETED.name()));
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any());
    }
} 