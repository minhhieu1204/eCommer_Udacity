package com.example.demo.services.impl;

import com.example.demo.model.dtos.CartRequest;
import com.example.demo.model.entities.Cart;
import com.example.demo.model.entities.Item;
import com.example.demo.model.entities.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.ItemRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.CartService;
import com.example.demo.utils.CommonUtil;
import com.example.demo.utils.JSONUtil;
import com.example.demo.utils.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;
    @Override
    public ResponseEntity addToCart(CartRequest cartRequest) {
        try{
            User user = userRepository.findByUsername(cartRequest.getUsername());
            if(CommonUtil.isEmpty(user)){
                throw new ResourceNotFoundException(Message.username_not_exist);
            }

            Item item = itemRepository.findById(cartRequest.getItemId()).orElse(null);
            if(CommonUtil.isEmpty(item)){
                throw new ResourceNotFoundException(Message.item_not_exist);
            }

            Cart cart = user.getCart();
            IntStream.range(0, cartRequest.getQuantity())
                    .forEach(i -> cart.addItem(item));
            logger.info("Add item to  cart success. CartId={}", cart.getId());
            return ResponseEntity.ok().body(cartRepository.save(cart));
        } catch(Exception e){
            logger.error("add item to cart fail. Cart={}." + e.getMessage(), cartRequest);
            return JSONUtil.buildError(e.getMessage());
        }
    }

    @Override
    public ResponseEntity removeFromCart(CartRequest cartRequest) {
        try{
            User user = userRepository.findByUsername(cartRequest.getUsername());
            if(CommonUtil.isEmpty(user)){
                throw new ResourceNotFoundException(Message.username_not_exist);
            }

            Item item = itemRepository.findById(cartRequest.getItemId()).orElse(null);
            if(CommonUtil.isEmpty(item)){
                throw new ResourceNotFoundException(Message.item_not_exist);
            }

            Cart cart = user.getCart();
            IntStream.range(0, cartRequest.getQuantity())
                    .forEach(i -> cart.removeItem(item));

            logger.info("remove item from cart success. CartId={}", cart.getId());
            return ResponseEntity.ok().body(cartRepository.save(cart));
        } catch (Exception e){
            logger.error("remove item from cart fail." + e.getMessage());
            return JSONUtil.buildError(e.getMessage());
        }
    }
}
