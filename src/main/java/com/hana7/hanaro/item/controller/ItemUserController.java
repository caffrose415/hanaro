package com.hana7.hanaro.item.controller;


import com.hana7.hanaro.item.dto.ItemSearchResponseDTO;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name="[사용자] 상품")
@RequestMapping("/user/items")
@RequiredArgsConstructor
public class ItemUserController {

	private final ItemService itemService;

	@GetMapping("/{id}")
	@Operation(summary = "아이템 아이디로 검색")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		log.info("Controller 사용자 : 아이템 id값으로 검색 id={}",id);
		Item item = itemService.getItemById(id);
		return ResponseEntity.ok(item);
	}

	@GetMapping
	@Operation(summary = "아이템 전체 검색")
	public ResponseEntity<List<Item>> getAllItems() {
		log.info("Controller 사용자 : 아이템 전체 검색");
		List<Item> items = itemService.getAllItems();
		return ResponseEntity.ok(items);
	}

	@GetMapping("/search")
	@Operation(summary = "아이템 검색")
	public ResponseEntity<List<ItemSearchResponseDTO>> search(@RequestParam("name") String name) {
		log.info("Controller 사용자 : 아이템 이름으로 검색");
		return ResponseEntity.ok(itemService.searchItemsByName(name));
	}
}
