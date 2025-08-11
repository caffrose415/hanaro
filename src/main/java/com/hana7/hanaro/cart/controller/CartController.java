package com.hana7.hanaro.cart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana7.hanaro.item.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.item.dto.ItemUpdateRequestDTO;
import com.hana7.hanaro.item.entity.Item;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;

	@PreAuthorize("hasRole('USER')")
	@Operation(summary = "아이템 등록")
	@PostMapping()
	public ResponseEntity<Item> createItem(@Valid ItemCreateRequestDTO requestDTO) {
		Item created = itemService.createItemWithImages(requestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		Item item = itemService.getItemById(id);
		return ResponseEntity.ok(item);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping
	public ResponseEntity<List<Item>> getAllItems() {
		List<Item> items = itemService.getAllItems();
		return ResponseEntity.ok(items);
	}

	@PreAuthorize("hasRole('USER')")
	@PutMapping("/{id}")
	public ResponseEntity<Item> updateItem(@Valid @RequestBody ItemUpdateRequestDTO requestDTO,@PathVariable Long id) {
		Item updatedItem = itemService.updateItem(requestDTO,id);
		return ResponseEntity.ok(updatedItem);
	}

	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
		itemService.deleteItem(id);
		return ResponseEntity.noContent().build();
	}
}
