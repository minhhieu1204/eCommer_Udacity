package com.example.demo.services.impl;

import com.example.demo.dtos.ItemDto;
import com.example.demo.entities.Item;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repositories.ItemRepository;
import com.example.demo.services.ItemService;
import com.example.demo.utils.CommonUtil;
import com.example.demo.utils.JSONUtil;
import com.example.demo.utils.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public ResponseEntity getItems() {
        List<Item> items = null;
        try {
            items = itemRepository.findAll();
        } catch (Exception e) {
            logger.error("get items fail." + e.getMessage());
            return JSONUtil.buildError(e.getMessage());
        }
        logger.info("get items success.");
        return ResponseEntity.ok().body(mapperUtil.mapToList(items, ItemDto.class));
    }

    @Override
    public ResponseEntity getItemById(Long itemId) {
        Item item = null;
        try {
            item = itemRepository.findById(itemId).orElse(null);
            if (CommonUtil.isEmpty(item)) {
                throw new ResourceNotFoundException("Item not exist.");
            }
        } catch (Exception e) {
            logger.error("get item by id={} fail." + e.getMessage(), itemId);
            return JSONUtil.buildError(e.getMessage());
        }
        logger.info("get items by id={} success.", itemId);
        return ResponseEntity.ok().body(mapperUtil.map(item, ItemDto.class));
    }

    @Override
    public ResponseEntity getItemsByName(String itemName) {
        itemName = "%" + itemName + "%";
        List<Item> items = null;
        try {
            items = itemRepository.findByNameLike(itemName);
        } catch (Exception e) {
            logger.error("get items by name={} fail." + e.getMessage(), itemName);
            return JSONUtil.buildError(e.getMessage());
        }
        logger.info("get items by name={} success.", itemName);
        return items.size() == 0 ? ResponseEntity.notFound().build()
                : ResponseEntity.ok().body(mapperUtil.mapToList(items, ItemDto.class));
    }
}
