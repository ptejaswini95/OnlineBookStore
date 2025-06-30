package com.bookstore.backend.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String isbn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 