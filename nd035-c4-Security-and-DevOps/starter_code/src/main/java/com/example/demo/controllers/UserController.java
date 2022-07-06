package com.example.demo.controllers;

import com.example.demo.config.security.UserDetailsServiceImpl;
import com.example.demo.model.dtos.CreateUserRequest;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;


	@GetMapping("/id/{id}")
	public ResponseEntity findById(@PathVariable Long id) {
		return userService.findById(id);
	}

	@GetMapping("/{username}")
	public ResponseEntity findByUserName(@PathVariable String username) {
		return userService.findByUsername(username);
	}

	@PostMapping("/create")
	public ResponseEntity createUser(@RequestBody CreateUserRequest createUserRequest) {
		return userService.createUser(createUserRequest);
	}
	
}
