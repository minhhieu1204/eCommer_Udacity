package com.example.demo.services;

import com.example.demo.dtos.CartRequest;
import com.example.demo.dtos.CreateUserRequest;
import org.springframework.http.ResponseEntity;

public interface CartService {

    ResponseEntity addToCart (CartRequest cartRequest);
    ResponseEntity removeFromCart (CartRequest cartRequest);
}
