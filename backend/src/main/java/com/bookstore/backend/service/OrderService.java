package com.bookstore.backend.service;

import com.bookstore.backend.dto.request.OrderRequest;
import com.bookstore.backend.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse createOrder(Long userId, OrderRequest request);
    OrderResponse getOrder(Long id);
    Page<OrderResponse> getUserOrders(Long userId, Pageable pageable);
    Page<OrderResponse> getAllOrders(Pageable pageable);
    OrderResponse updateOrderStatus(Long id, String status);
} 