package com.example.demo.controllers;

import com.example.demo.TestUtil;
import com.example.demo.model.dtos.CreateUserRequest;
import com.example.demo.model.entities.Cart;
import com.example.demo.model.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import com.example.demo.services.impl.UserServiceImpl;
import com.example.demo.utils.MapperUtil;
import com.example.demo.utils.Message;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private MapperUtil mapperUtil = mock(MapperUtil.class);

    private UserService userService = mock(UserService.class);

    @Before
    public void setUp() {
        userController = new UserController();
        userService = new UserServiceImpl();
        TestUtil.injectObjects(userController, "userService", userService);
        TestUtil.injectObjects(userService, "bCryptPasswordEncoder", encoder);
        TestUtil.injectObjects(userService, "mapperUtil", mapperUtil);
        TestUtil.injectObjects(userService, "userRepository", userRepository);
        when(mapperUtil.map(any(), any())).thenReturn(initUser());
        when(userRepository.save(any())).thenReturn(initUser());
    }

    public User initUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(new Cart());
        return user;
    }

    @Test
    public void createUser() {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setPasswordConfirm("testPassword");

        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void createUserConfirmPasswordFail() throws JSONException {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setPasswordConfirm("testPassword2");

        ResponseEntity<String> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        String message = new JSONObject(response.getBody()).getString("message");
        assertEquals(Message.password_not_same, message);
    }

    @Test
    public void createUserPasswordShort() throws JSONException {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("pass");
        request.setPasswordConfirm("pass");

        ResponseEntity<String> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        String message = new JSONObject(response.getBody()).getString("message");
        assertEquals(Message.password_short, message);
    }

    @Test
    public void findById() {
        long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(initUser()));

        ResponseEntity<User> response = userController.findById(id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    public void findUserNotExist() throws JSONException {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<String> response = userController.findByUserName("test");
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

        String message = new JSONObject(response.getBody()).getString("message");
        assertEquals(Message.username_not_exist, message);
    }

    @Test
    public void findByUserName() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(initUser());

        ResponseEntity<User> response = userController.findByUserName("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        long id = 1L;
        assertEquals(id, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }




}
