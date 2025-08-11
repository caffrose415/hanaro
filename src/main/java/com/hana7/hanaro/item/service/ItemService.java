package com.hana7.hanaro.item.service;

import com.hana7.hanaro.item.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.item.dto.ItemUpdateRequestDTO;
import com.hana7.hanaro.item.entity.Item;

import java.util.List;

public interface ItemService {
    Item createItem(ItemCreateRequestDTO requestDTO);
    Item getItemById(Long id);
    List<Item> getAllItems();
    Item updateItem(ItemUpdateRequestDTO requestDTO);
    void deleteItem(Long id);
}
