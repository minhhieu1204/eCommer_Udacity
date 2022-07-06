package com.example.demo.services;

import com.example.demo.model.dtos.CartRequest;
import org.springframework.http.ResponseEntity;

public interface CartService {

    ResponseEntity addToCart (CartRequest cartRequest);
    ResponseEntity removeFromCart (CartRequest cartRequest);
}
