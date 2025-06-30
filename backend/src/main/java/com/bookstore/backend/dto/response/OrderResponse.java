package com.bookstore.backend.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private String status;
    private UserResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 