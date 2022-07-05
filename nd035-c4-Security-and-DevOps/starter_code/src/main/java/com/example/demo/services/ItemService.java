package com.example.demo.services;

import org.springframework.http.ResponseEntity;

public interface ItemService {

    ResponseEntity getItems ();
    ResponseEntity getItemById (Long itemId);
    ResponseEntity getItemsByName (String itemName);
}
