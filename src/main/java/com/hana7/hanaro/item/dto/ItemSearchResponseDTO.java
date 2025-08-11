package com.hana7.hanaro.item.dto;

import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.entity.ItemImage;

public record ItemSearchResponseDTO (
	String name,

	int price,

	int stock
){
	public static ItemSearchResponseDTO from(Item item) {
		return new ItemSearchResponseDTO(
			item.getName(),
			item.getPrice(),
			item.getStock()
		);
	}
}
