package com.example.demo.dtos;

import com.example.demo.entities.Item;
import com.example.demo.entities.User;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.util.List;

public class CartDto {
    private Long id;

    private List<ItemDto> items;
    private UserDto user;

    private BigDecimal total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
