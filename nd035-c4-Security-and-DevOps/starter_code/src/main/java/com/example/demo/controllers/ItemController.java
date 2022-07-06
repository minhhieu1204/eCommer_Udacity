package com.example.demo.controllers;

import java.util.List;

import com.example.demo.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entities.Item;

@RestController
@RequestMapping(value = "/api/item", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@GetMapping
	public ResponseEntity getItems() {
		return itemService.getItems();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity getItemById(@PathVariable Long id) {
		return itemService.getItemById(id);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity getItemsByName(@PathVariable String name) {
		return itemService.getItemsByName(name);
	}
	
}
