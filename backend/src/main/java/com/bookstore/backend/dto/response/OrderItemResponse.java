package com.bookstore.backend.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private Long id;
    private BookResponse book;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
} 