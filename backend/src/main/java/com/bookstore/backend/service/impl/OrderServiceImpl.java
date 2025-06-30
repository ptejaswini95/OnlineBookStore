package com.bookstore.backend.service.impl;

import com.bookstore.backend.dto.request.OrderItemRequest;
import com.bookstore.backend.dto.request.OrderRequest;
import com.bookstore.backend.dto.response.BookResponse;
import com.bookstore.backend.dto.response.OrderItemResponse;
import com.bookstore.backend.dto.response.OrderResponse;
import com.bookstore.backend.dto.response.UserResponse;
import com.bookstore.backend.entity.Book;
import com.bookstore.backend.entity.Order;
import com.bookstore.backend.entity.OrderItem;
import com.bookstore.backend.entity.OrderStatus;
import com.bookstore.backend.entity.User;
import com.bookstore.backend.repository.BookRepository;
import com.bookstore.backend.repository.OrderRepository;
import com.bookstore.backend.repository.UserRepository;
import com.bookstore.backend.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    @CacheEvict(value = {"orders", "books"}, allEntries = true)
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Book book = bookRepository.findById(itemRequest.getBookId())
                    .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + itemRequest.getBookId()));

            if (book.getStock() < itemRequest.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for book: " + book.getTitle());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(book.getPrice());
            orderItem.setSubtotal(book.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));

            book.setStock(book.getStock() - itemRequest.getQuantity());
            bookRepository.save(book);

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        return convertToResponse(orderRepository.save(order));
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public OrderResponse getOrder(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    @Override
    @Cacheable(value = "orders", key = "'user-' + #userId")
    public Page<OrderResponse> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable)
                .map(this::convertToResponse);
    }

    @Override
    @Cacheable(value = "orders", key = "'all'")
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Override
    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        order.setStatus(OrderStatus.valueOf(status));
        return convertToResponse(orderRepository.save(order));
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus().name());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        UserResponse userResponse = new UserResponse();
        userResponse.setId(order.getUser().getId());
        userResponse.setUsername(order.getUser().getUsername());
        userResponse.setEmail(order.getUser().getEmail());
        userResponse.setRole(order.getUser().getRole().name());
        response.setUser(userResponse);

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPrice(item.getPrice());
            itemResponse.setSubtotal(item.getSubtotal());

            BookResponse bookResponse = new BookResponse();
            bookResponse.setId(item.getBook().getId());
            bookResponse.setTitle(item.getBook().getTitle());
            bookResponse.setAuthor(item.getBook().getAuthor());
            bookResponse.setPrice(item.getBook().getPrice());
            itemResponse.setBook(bookResponse);

            itemResponses.add(itemResponse);
        }
        response.setItems(itemResponses);

        return response;
    }
} 