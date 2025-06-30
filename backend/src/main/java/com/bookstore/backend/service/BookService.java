package com.bookstore.backend.service;

import com.bookstore.backend.dto.request.BookRequest;
import com.bookstore.backend.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponse createBook(BookRequest request);
    BookResponse updateBook(Long id, BookRequest request);
    void deleteBook(Long id);
    BookResponse getBook(Long id);
    Page<BookResponse> getAllBooks(Pageable pageable);
    Page<BookResponse> searchBooks(String query, Pageable pageable);
    Page<BookResponse> getAvailableBooks(Pageable pageable);
    Page<BookResponse> getLowStockBooks(int threshold, Pageable pageable);
} 