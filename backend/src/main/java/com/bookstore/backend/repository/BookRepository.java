package com.bookstore.backend.repository;

import com.bookstore.backend.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    Optional<Book> findByIsbn(String isbn);
    
    List<Book> findByCategoryIgnoreCase(String category);
    
    @Query("SELECT b FROM Book b WHERE b.stock > 0")
    List<Book> findAvailableBooks();
    
    @Query("SELECT b FROM Book b WHERE b.stock <= :threshold")
    List<Book> findLowStockBooks(int threshold);

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);
    Page<Book> findByStockGreaterThan(int stock, Pageable pageable);
    Page<Book> findByStockLessThanEqual(int threshold, Pageable pageable);
} 