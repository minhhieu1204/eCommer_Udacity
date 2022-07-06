package com.example.demo.services.impl;

import com.example.demo.model.dtos.CreateUserRequest;
import com.example.demo.model.entities.Cart;
import com.example.demo.model.entities.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import com.example.demo.utils.CommonUtil;
import com.example.demo.utils.JSONUtil;
import com.example.demo.utils.MapperUtil;
import com.example.demo.utils.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public ResponseEntity createUser(CreateUserRequest createUserRequest) {
        User user = null;
        try{
            if(!createUserRequest.getPassword().equals(createUserRequest.getPasswordConfirm())){
                throw new ResourceNotFoundException(Message.password_not_same);
            }
            if(createUserRequest.getPassword().length() < 7){
                throw new ResourceNotFoundException(Message.password_short);
            }
            if(CommonUtil.isPresent(userRepository.findByUsername(createUserRequest.getUsername()))){
                throw new ResourceNotFoundException(Message.username_existed);
            }
            user = mapperUtil.map(createUserRequest, User.class);
            user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
            user.setCart(new Cart());
            userRepository.save(user);
        } catch (Exception e){
            logger.error("Create user={} fail. " + e.getMessage(), createUserRequest.getUsername());
            return JSONUtil.buildError(e.getMessage());
        }
        logger.info("Create user={} success.", user.getUsername());
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity findById(Long userId) {
        User user = null;
        try{
            user = userRepository.findById(userId).orElse(null);
            if(CommonUtil.isEmpty(user)){
                throw new ResourceNotFoundException(Message.username_not_exist);
            }

        } catch(Exception e){
            logger.error("find user by id={} fail. " + e.getMessage(), userId);
            return JSONUtil.buildError(e.getMessage());
        }
        logger.info("find user={} by id={} success.", user.getUsername(), userId);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity findByUsername(String username) {
        User user = null;
        try{
            user = userRepository.findByUsername(username);
            if(CommonUtil.isEmpty(user)){
                throw new ResourceNotFoundException(Message.username_not_exist);
            }

        } catch(Exception e){
            logger.error("find user by username={} fail. " + e.getMessage(), username);
            return JSONUtil.buildError(e.getMessage());
        }
        logger.info("find user={} by username={} success.", user.getUsername(), username);
        return ResponseEntity.ok(user);
    }
}
