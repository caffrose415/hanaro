package com.hana7.hanaro.item.repository;

import com.hana7.hanaro.RepositoryTest;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.entity.ItemImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest extends RepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    private Item testItem;

    @BeforeEach
    void setUp() {
        List<ItemImage> itemImages = new ArrayList<>();
        Item item = Item.builder()
                .name("테스트 상품")
                .price(10000)
                .stock(100)
                .build();

        for (int j = 0; j < 3; j++) {
            ItemImage itemImage = ItemImage.builder()
                    .imgUrl("/images/item/test_" + j + ".jpg")
                    .item(item)
                    .build();
            itemImages.add(itemImage);
        }
        item.setItemImages(itemImages);

        testItem = itemRepository.save(item);
    }

    @Test
    @Order(1)
    void createItemTest() {
        List<ItemImage> itemImages = new ArrayList<>();
        Item item = Item.builder()
            .name("새로운 상품")
            .price(20000)
            .stock(50)
            .build();

        for (int j = 0; j < 2; j++) {
            ItemImage itemImage = ItemImage.builder()
                .imgUrl("/images/item/new_" + j + ".jpg")
                .item(item)
                .build();
            itemImages.add(itemImage);
        }
        item.setItemImages(itemImages);

        Item savedItem = itemRepository.save(item);

        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getName()).isEqualTo("새로운 상품");
        assertThat(savedItem.getPrice()).isEqualTo(20000);
        assertThat(savedItem.getStock()).isEqualTo(50);
        assertThat(savedItem.getItemImages()).hasSize(2);
    }

    @Test
	@Order(2)
    void findItemTest() {
        Item foundItem = itemRepository.findById(testItem.getId()).orElseThrow();

        assertThat(foundItem.getName()).isEqualTo("테스트 상품");
        assertThat(foundItem.getPrice()).isEqualTo(10000);
        assertThat(foundItem.getItemImages()).hasSize(3);
    }

    @Test
	@Order(3)
    void updateItemTest() {
        Item savedItem = itemRepository.findById(testItem.getId()).orElseThrow();
        savedItem.setName("수정된 상품");
        savedItem.setPrice(15000);
        itemRepository.save(savedItem);

        Item updatedItem = itemRepository.findById(testItem.getId()).orElseThrow();
        assertThat(updatedItem.getName()).isEqualTo("수정된 상품");
        assertThat(updatedItem.getPrice()).isEqualTo(15000);
    }

    @Test
	@Order(4)
    void deleteItemTest() {
        itemRepository.deleteById(testItem.getId());

        assertThat(itemRepository.findById(testItem.getId())).isEmpty();
    }
}
