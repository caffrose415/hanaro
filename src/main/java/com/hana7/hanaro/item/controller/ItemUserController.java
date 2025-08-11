package com.hana7.hanaro.item.controller;

import com.hana7.hanaro.item.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.item.dto.ItemSearchResponseDTO;
import com.hana7.hanaro.item.dto.ItemUpdateRequestDTO;
import com.hana7.hanaro.item.dto.StockAdjustRequestDTO;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.service.ItemService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="사용자 상품관련")
@RequestMapping("/user/items")
@RequiredArgsConstructor
public class ItemUserController {

	private final ItemService itemService;

	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		Item item = itemService.getItemById(id);
		return ResponseEntity.ok(item);
	}

	@GetMapping
	public ResponseEntity<List<Item>> getAllItems() {
		List<Item> items = itemService.getAllItems();
		return ResponseEntity.ok(items);
	}

	@GetMapping("/search")
	public ResponseEntity<List<ItemSearchResponseDTO>> search(@RequestParam("name") String name) {
		return ResponseEntity.ok(itemService.searchItemsByName(name));
	}
}
