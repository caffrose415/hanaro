package com.hana7.hanaro.item.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import com.hana7.hanaro.RepositoryTest;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.entity.ItemImage;

class ItemRepositoryTest extends RepositoryTest {
	private static final int LIMIT = 10;

	@Autowired
	private ItemRepository itemRepository;

	@Test
	@Commit
	@Order(1)
	void addTest() {
		long count = itemRepository.count();

		List<Item> list = Stream.iterate(0, i -> i + 1)
			.limit(LIMIT)
			.map(i -> {
				Item item = Item.builder()
					.name("과자 " + i)
					.price(10000)
					.stock(100)
					.build();

				List<ItemImage> itemImages = new ArrayList<>();
				for (int j = 0; j < 3; j++) {
					ItemImage itemImage = ItemImage.builder()
						.imgUrl("/images/item/test" + i + "_" + j + ".jpg")
						.item(item)
						.build();
					itemImages.add(itemImage);
				}
				item.setItemImages(itemImages);
				return item;
			})
			.toList();

		itemRepository.saveAll(list);
	}
}
