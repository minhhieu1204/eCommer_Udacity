package com.example.demo.services;

import com.example.demo.model.dtos.CreateUserRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity createUser (CreateUserRequest CreateUserRequest);
    ResponseEntity findById (Long userId);
    ResponseEntity findByUsername (String username);
}
