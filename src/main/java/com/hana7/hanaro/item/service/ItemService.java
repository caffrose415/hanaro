package com.hana7.hanaro.item.service;

import com.hana7.hanaro.item.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.item.dto.ItemSearchResponseDTO;
import com.hana7.hanaro.item.dto.ItemUpdateRequestDTO;
import com.hana7.hanaro.item.entity.Item;

import java.util.List;


public interface ItemService {
    Item createItemWithImages(ItemCreateRequestDTO requestDTO);
    Item getItemById(Long id);
    List<Item> getAllItems();
    Item updateItem(ItemUpdateRequestDTO requestDTO,long id);
    void deleteItem(Long id);

    void deleteImage(Long itemId, Long imageId);

    Item adjustStock(Long id, int cnt);
    List<ItemSearchResponseDTO> searchItemsByName(String name);
}
