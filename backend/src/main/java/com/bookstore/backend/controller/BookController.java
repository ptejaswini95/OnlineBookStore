package com.bookstore.backend.controller;

import com.bookstore.backend.entity.Book;
import com.bookstore.backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;

    // Get all books, with optional category filter
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(value = "category", required = false) String category) {
        List<Book> books;
        if (category != null && !category.isEmpty()) {
            books = bookRepository.findByCategoryIgnoreCase(category);
        } else {
            books = bookRepository.findAll();
        }
        return ResponseEntity.ok(books);
    }

    // Get featured books (first 6 for demo)
    @GetMapping("/featured")
    public ResponseEntity<List<Book>> getFeaturedBooks() {
        List<Book> books = bookRepository.findAll();
        List<Book> featured = books.stream().limit(6).collect(Collectors.toList());
        return ResponseEntity.ok(featured);
    }

    // Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add a new book (admin only)
    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')") // Uncomment if using method security
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book saved = bookRepository.save(book);
        return ResponseEntity.ok(saved);
    }

    // Update a book (admin only)
    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookRepository.findById(id)
                .map(existing -> {
                    book.setId(id);
                    Book updated = bookRepository.save(book);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a book (admin only)
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 