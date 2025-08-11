package com.hana7.hanaro.item.service;

import com.hana7.hanaro.item.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.item.dto.ItemUpdateRequestDTO;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Item createItem(ItemCreateRequestDTO requestDTO) {
        Item item = Item.builder()
                .name(requestDTO.name())
                .price(requestDTO.price())
                .stock(requestDTO.stock())
                .build();
        return itemRepository.save(item);
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    @Transactional
    public Item updateItem(ItemUpdateRequestDTO requestDTO) {
        Item item = itemRepository.findById(requestDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        item.setName(requestDTO.name());
        item.setPrice(requestDTO.price());
        item.setStock(requestDTO.stock());

        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다.");
        }
        itemRepository.deleteById(id);
    }
}
