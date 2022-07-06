package com.example.demo.controllers;

import com.example.demo.TestUtil;
import com.example.demo.model.dtos.CartRequest;
import com.example.demo.model.entities.Cart;
import com.example.demo.model.entities.Item;
import com.example.demo.model.entities.User;
import com.example.demo.model.entities.UserOrder;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.OrderService;
import com.example.demo.services.impl.OrderServiceImpl;
import com.example.demo.utils.Message;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private OrderService orderService;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private Item initItem(long id) {
        Item item = new Item();
        item.setId(id);
        item.setName("Item " + id);
        item.setDescription("Description " + id);
        item.setPrice(new BigDecimal(1.5 * id));
        return item;
    }

    private Cart addCart(Cart cart, CartRequest cartRequest, Item item) {
        IntStream.range(0, cartRequest.getQuantity())
                .forEach(i -> cart.addItem(item));
        cart.setId(1L);
        return cart;
    }

    private CartRequest createCartRequest() {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setUsername("test");
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(2);
        return cartRequest;
    }

    public User initUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");
        Cart cart = addCart(new Cart(), createCartRequest(), initItem(1L));
        cart.setUser(user);
        user.setCart(cart);
        return user;
    }

    public UserOrder initOrder() {
        User user = initUser();
        UserOrder order = UserOrder.createFromCart(user.getCart());
        return order;
    }

    @Before
    public void setUp() {
        orderController = new OrderController();
        orderService = new OrderServiceImpl();
        TestUtil.injectObjects(orderController, "orderService", orderService);
        TestUtil.injectObjects(orderService, "userRepository", userRepository);
        TestUtil.injectObjects(orderService, "orderRepository", orderRepository);
        when(userRepository.findByUsername(any())).thenReturn(initUser());
    }

    @Test
    public void submitOrder (){
        User user = initUser();
        when(orderRepository.save(any())).thenReturn(UserOrder.createFromCart(user.getCart()));

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder orderActual = response.getBody();

        assertEquals(user.getUsername(), orderActual.getUser().getUsername());
        assertEquals(user.getCart().getItems().get(0), orderActual.getItems().get(0));
    }
    @Test
    public void submitOrderFail () throws JSONException {

        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<String> response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

        String message = new JSONObject(response.getBody()).getString("message");
        assertEquals(Message.username_not_exist, message);
    }

    @Test
    public void historyOfUser (){
        User user = initUser();
        when(orderRepository.findByUser(any())).thenReturn(Arrays.asList(UserOrder.createFromCart(user.getCart())));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> ordersActual = response.getBody();

        assertTrue(ordersActual.size()>0);
        assertEquals(user.getUsername(), ordersActual.get(0).getUser().getUsername());
    }

    @Test
    public void historyOfUserFail () throws JSONException {

        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<String> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

        String message = new JSONObject(response.getBody()).getString("message");
        assertEquals(Message.username_not_exist, message);
    }

}
