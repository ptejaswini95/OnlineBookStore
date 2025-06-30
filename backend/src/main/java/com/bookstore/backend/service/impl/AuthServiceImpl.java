package com.bookstore.backend.service.impl;

import com.bookstore.backend.dto.request.LoginRequest;
import com.bookstore.backend.dto.request.RegisterRequest;
import com.bookstore.backend.dto.response.AuthResponse;
import com.bookstore.backend.entity.User;
import com.bookstore.backend.entity.UserRole;
import com.bookstore.backend.repository.UserRepository;
import com.bookstore.backend.security.JwtTokenProvider;
import com.bookstore.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        try {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new IllegalStateException("Username is already taken");
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalStateException("Email is already in use");
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(UserRole.USER);

            userRepository.save(user);

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            return AuthResponse.builder()
                    .token(jwt)
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            return AuthResponse.builder()
                    .token(jwt)
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("Login failed: " + e.getMessage());
        }
    }
} 