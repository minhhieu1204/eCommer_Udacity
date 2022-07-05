package com.example.demo.services;

import com.example.demo.dtos.CreateUserRequest;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity submit (String username);
    ResponseEntity getOrders (String username);
}
