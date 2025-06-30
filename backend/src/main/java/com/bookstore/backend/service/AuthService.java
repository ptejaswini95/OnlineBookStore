package com.bookstore.backend.service;

import com.bookstore.backend.dto.request.LoginRequest;
import com.bookstore.backend.dto.request.RegisterRequest;
import com.bookstore.backend.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
} 