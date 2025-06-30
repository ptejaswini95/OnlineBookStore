package com.bookstore.backend.service;

import com.bookstore.backend.dto.request.BookRequest;
import com.bookstore.backend.dto.response.BookResponse;
import com.bookstore.backend.entity.Book;
import com.bookstore.backend.repository.BookRepository;
import com.bookstore.backend.service.impl.BookServiceImpl;
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
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;
    private BookRequest bookRequest;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setDescription("Test Description");
        testBook.setPrice(new BigDecimal("19.99"));
        testBook.setStock(10);
        testBook.setIsbn("1234567890");

        bookRequest = new BookRequest();
        bookRequest.setTitle("Test Book");
        bookRequest.setAuthor("Test Author");
        bookRequest.setDescription("Test Description");
        bookRequest.setPrice(new BigDecimal("19.99"));
        bookRequest.setStock(10);
        bookRequest.setIsbn("1234567890");
    }

    @Test
    void createBook_Success() {
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse response = bookService.createBook(bookRequest);

        assertNotNull(response);
        assertEquals(testBook.getTitle(), response.getTitle());
        assertEquals(testBook.getAuthor(), response.getAuthor());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponse response = bookService.updateBook(1L, bookRequest);

        assertNotNull(response);
        assertEquals(testBook.getTitle(), response.getTitle());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(1L, bookRequest));
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void getBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        BookResponse response = bookService.getBook(1L);

        assertNotNull(response);
        assertEquals(testBook.getTitle(), response.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.getBook(1L));
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getAllBooks_Success() {
        List<Book> books = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(books);
        Pageable pageable = PageRequest.of(0, 10);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<BookResponse> response = bookService.getAllBooks(pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    void searchBooks_Success() {
        List<Book> books = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(books);
        Pageable pageable = PageRequest.of(0, 10);

        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase("test", "test", pageable))
                .thenReturn(bookPage);

        Page<BookResponse> response = bookService.searchBooks("test", pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        verify(bookRepository, times(1))
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase("test", "test", pageable);
    }
} 