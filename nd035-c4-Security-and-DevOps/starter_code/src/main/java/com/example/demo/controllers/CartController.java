package com.example.demo.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import com.example.demo.dtos.CartRequest;
import com.example.demo.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Cart;
import com.example.demo.entities.Item;
import com.example.demo.entities.User;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.ItemRepository;
import com.example.demo.repositories.UserRepository;

@RestController
@RequestMapping(value = "/api/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody CartRequest request) {
		return cartService.addToCart(request);
	}

	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody CartRequest request) {
		return cartService.removeFromCart(request);
	}
		
}
