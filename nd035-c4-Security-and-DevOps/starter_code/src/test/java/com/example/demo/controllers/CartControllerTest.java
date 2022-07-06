package com.example.demo.controllers;

import com.example.demo.TestUtil;
import com.example.demo.model.dtos.CartRequest;
import com.example.demo.model.entities.Cart;
import com.example.demo.model.entities.Item;
import com.example.demo.model.entities.User;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.ItemRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.impl.CartServiceImpl;
import com.example.demo.utils.Message;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    private CartServiceImpl cartService;

    private UserRepository userRepository = mock(UserRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    public User initUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(new Cart());
        return user;
    }

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
        cart.setUser(initUser());
        return cart;
    }

    private Cart removeCart(Cart cart, CartRequest cartRequest, Item item) {
        IntStream.range(0, cartRequest.getQuantity())
                .forEach(i -> cart.removeItem(item));
        return cart;
    }

    private CartRequest createCartRequest() {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setUsername("test");
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(2);
        return cartRequest;
    }

    @Before
    public void setUp() {
        cartController = new CartController();
        cartService = new CartServiceImpl();
        TestUtil.injectObjects(cartController, "cartService", cartService);
        TestUtil.injectObjects(cartService, "userRepository", userRepository);
        TestUtil.injectObjects(cartService, "itemRepository", itemRepository);
        TestUtil.injectObjects(cartService, "cartRepository", cartRepository);
        when(userRepository.findByUsername(any())).thenReturn(initUser());
        when(itemRepository.findById(1L)).thenReturn(Optional.of(initItem(1L)));
    }

    @Test
    public void addItemToCart() {
        Cart cart = addCart(initUser().getCart(), createCartRequest(), initItem(1L));
        when(cartRepository.save(any())).thenReturn(cart);
        ResponseEntity<Cart> response = cartController.addTocart(createCartRequest());

        Cart cartActual = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        assertEquals(cart.getTotal(), cartActual.getTotal());
        assertEquals(initUser().getUsername(), cartActual.getUser().getUsername());
    }

    @Test
    public void removeItemFromCart() {
        Cart cart = addCart(initUser().getCart(), createCartRequest(), initItem(1L));

        CartRequest cartRequest = createCartRequest();
        cartRequest.setQuantity(1);
        when(cartRepository.save(any())).thenReturn(removeCart(cart, cartRequest, initItem(1L)));
        ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);

        Cart cartActual = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        assertEquals(cart.getTotal(), cartActual.getTotal());
        assertEquals(initUser().getUsername(), cartActual.getUser().getUsername());
    }

    @Test
    public void addCartFailByUsernameNotExist() throws JSONException {

        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<String> response = cartController.addTocart(createCartRequest());

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        String message = new JSONObject(response.getBody()).getString("message");
        assertEquals(Message.username_not_exist, message);
    }

    @Test
    public void addCartFailByItemNotExist() throws JSONException {

        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<String> response = cartController.addTocart(createCartRequest());

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        String message = new JSONObject(response.getBody()).getString("message");
        assertEquals(Message.item_not_exist, message);
    }


}
