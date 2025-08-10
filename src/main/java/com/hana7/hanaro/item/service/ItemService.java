package com.hana7.hanaro.item.service;

import com.hana7.hanaro.item.entity.Item;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> getItems(String search);
    Optional<Item> getItemById(Long id);
}
