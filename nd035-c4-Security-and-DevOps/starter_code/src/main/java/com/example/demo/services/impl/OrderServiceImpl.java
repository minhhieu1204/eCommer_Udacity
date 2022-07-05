package com.example.demo.services.impl;

import com.example.demo.dtos.OrderDto;
import com.example.demo.entities.User;
import com.example.demo.entities.UserOrder;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.OrderService;
import com.example.demo.utils.JSONUtil;
import com.example.demo.utils.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public ResponseEntity submit(String username) {
        try{
            User user = userRepository.findByUsername(username);
            if(user == null) {
               throw new ResourceNotFoundException("Username not found.");
            }
            UserOrder order = UserOrder.createFromCart(user.getCart());
            order = orderRepository.save(order);
            logger.info("order of username={} success.", username);
            return ResponseEntity.ok().body(mapperUtil.map(order, OrderDto.class));

        } catch (Exception e){
            logger.error("order of username={} fail." + e.getMessage(), username);
            return JSONUtil.buildError(e.getMessage());
        }
    }

    @Override
    public ResponseEntity getOrders(String username) {
        try{
            User user = userRepository.findByUsername(username);
            if(user == null) {
                throw new ResourceNotFoundException("Username not found.");
            }
            logger.info("get orders of username={} success.", username);
            return ResponseEntity.ok().body(mapperUtil.mapToList(orderRepository.findByUser(user), OrderDto.class));

        } catch (Exception e){
            logger.error("get orders of username={} fail." + e.getMessage(), username);
            return JSONUtil.buildError(e.getMessage());
        }
    }
}
