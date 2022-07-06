package com.example.demo.controllers;

import com.example.demo.TestUtil;
import com.example.demo.model.entities.Item;
import com.example.demo.repositories.ItemRepository;
import com.example.demo.services.ItemService;
import com.example.demo.services.impl.ItemServiceImpl;
import com.example.demo.utils.Message;
import org.assertj.core.util.Lists;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private ItemService itemService = mock(ItemService.class);

    private Item initItem(long id) {
        Item item = new Item();
        item.setId(id);
        item.setName("Item " + id);
        item.setDescription("Description " + id);
        item.setPrice(new BigDecimal(1.5 * id));
        return item;
    }

    @Before
    public void setUp() {
        itemController = new ItemController();
        itemService = new ItemServiceImpl();
        TestUtil.injectObjects(itemController, "itemService", itemService);
        TestUtil.injectObjects(itemService, "itemRepository", itemRepository);
    }

    @Test
    public void getAllItem() {
        List<Long> ids = Lists.newArrayList(LongStream.range(1, 2).iterator());
        when(itemRepository.findAll()).thenReturn(ids.stream().map(x -> {
            return initItem(x);
        }).collect(Collectors.toList()));

        List<Item> expectItens = ids.stream().map(x -> {
            return initItem(x);
        }).collect(Collectors.toList());

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectItens, response.getBody());
    }

    @Test
    public void getItemById() {
        long id = 1;
        when(itemRepository.findById(id)).thenReturn(Optional.of(initItem(id)));

        ResponseEntity<Item> response = itemController.getItemById(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(initItem(id), response.getBody());
    }

    @Test
    public void getItemByName() {
        long id = 1;
        List<Item> items = Arrays.asList(initItem(id));
        when(itemRepository.findByNameLike(any())).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(Long.toString(id));

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(initItem(id), response.getBody().get(0));
    }

    @Test
    public void getItemByIdFail() throws JSONException {
        long id = 1;
        when(itemRepository.findByNameLike(any())).thenReturn(null);

        ResponseEntity<String> response = itemController.getItemById(id);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        String message = new JSONObject(response.getBody()).getString("message");
        assertEquals(Message.item_not_exist, message);
    }
}
