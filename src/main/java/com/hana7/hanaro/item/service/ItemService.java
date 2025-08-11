package com.hana7.hanaro.item.service;

import com.hana7.hanaro.item.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.item.dto.ItemUpdateRequestDTO;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.entity.ItemImage;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ItemService {
    Item createItemWithImages(ItemCreateRequestDTO requestDTO);
    Item getItemById(Long id);
    List<Item> getAllItems();
    Item updateItem(ItemUpdateRequestDTO requestDTO);
    void deleteItem(Long id);

    void deleteImage(Long itemId, Long imageId);

    Item adjustStock(Long id, int cnt);
}
