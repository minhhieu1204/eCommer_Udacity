package com.example.demo.controllers;

import java.util.List;

import com.example.demo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entities.UserOrder;

@RestController
@RequestMapping(value = "/api/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/submit/{username}")
	public ResponseEntity submit(@PathVariable String username) {
		return orderService.submit(username);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity getOrdersForUser(@PathVariable String username) {
		return orderService.getOrders(username);
	}
}
