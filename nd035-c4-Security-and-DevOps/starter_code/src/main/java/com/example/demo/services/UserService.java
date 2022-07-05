package com.example.demo.services;

import com.example.demo.dtos.CreateUserRequest;
import com.example.demo.dtos.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity createUser (CreateUserRequest CreateUserRequest);
    ResponseEntity findById (Long userId);
    ResponseEntity findByUsername (String username);
}
