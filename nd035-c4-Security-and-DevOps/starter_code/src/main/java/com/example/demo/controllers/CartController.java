package com.example.demo.controllers;

import com.example.demo.model.dtos.CartRequest;
import com.example.demo.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entities.Cart;

@RestController
@RequestMapping(value = "/api/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@PostMapping("/addToCart")
	public ResponseEntity addTocart(@RequestBody CartRequest request) {
		return cartService.addToCart(request);
	}

	@PostMapping("/removeFromCart")
	public ResponseEntity removeFromcart(@RequestBody CartRequest request) {
		return cartService.removeFromCart(request);
	}
		
}
