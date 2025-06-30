package com.bookstore.backend.service.impl;

import com.bookstore.backend.dto.request.BookRequest;
import com.bookstore.backend.dto.response.BookResponse;
import com.bookstore.backend.entity.Book;
import com.bookstore.backend.repository.BookRepository;
import com.bookstore.backend.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public BookResponse createBook(BookRequest request) {
        Book book = new Book();
        updateBookFromRequest(book, request);
        return convertToResponse(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        updateBookFromRequest(book, request);
        return convertToResponse(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public BookResponse getBook(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
    }

    @Override
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Override
    public Page<BookResponse> searchBooks(String query, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query, pageable)
                .map(this::convertToResponse);
    }

    @Override
    public Page<BookResponse> getAvailableBooks(Pageable pageable) {
        return bookRepository.findByStockGreaterThan(0, pageable)
                .map(this::convertToResponse);
    }

    @Override
    public Page<BookResponse> getLowStockBooks(int threshold, Pageable pageable) {
        return bookRepository.findByStockLessThanEqual(threshold, pageable)
                .map(this::convertToResponse);
    }

    private void updateBookFromRequest(Book book, BookRequest request) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock());
        book.setIsbn(request.getIsbn());
    }

    private BookResponse convertToResponse(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setDescription(book.getDescription());
        response.setPrice(book.getPrice());
        response.setStock(book.getStock());
        response.setIsbn(book.getIsbn());
        response.setCreatedAt(book.getCreatedAt());
        response.setUpdatedAt(book.getUpdatedAt());
        return response;
    }
} 