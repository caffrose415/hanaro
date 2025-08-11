package com.hana7.hanaro.item.dto;

import java.util.List;

import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.entity.ItemImage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ItemCreateResponseDTO (
	String name,

	int price,

	int stock,
	
	List<String> imageUrls

){
	public static ItemCreateResponseDTO fromCreate(Item item) {
		List<String> urls = (item.getItemImages() == null)
			? List.of()
			: item.getItemImages().stream()
			.map(ItemImage::getImgUrl)
			.toList();

		return new ItemCreateResponseDTO(
			item.getName(),
			item.getPrice(),
			item.getStock(),
			urls
		);
	}
}
