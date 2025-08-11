package com.hana7.hanaro.item.controller;

import com.hana7.hanaro.item.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.item.dto.ItemUpdateRequestDTO;
import com.hana7.hanaro.item.dto.StockAdjustRequestDTO;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.entity.ItemImage;
import com.hana7.hanaro.item.service.ItemService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name="관리자 상품관련")
@RequestMapping("/admin/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Item> createItem(@Valid ItemCreateRequestDTO requestDTO) {
        Item created = itemService.createItemWithImages(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Item item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Item> updateItem(@Valid @RequestBody ItemUpdateRequestDTO requestDTO) {
        Item updatedItem = itemService.updateItem(requestDTO);
        return ResponseEntity.ok(updatedItem);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<Void> deleteItemImage(@PathVariable Long id, @PathVariable Long imageId) {
        itemService.deleteImage(id, imageId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Item> adjustStock(@PathVariable Long id,
        @Valid @RequestBody StockAdjustRequestDTO dto) {
        return ResponseEntity.ok(itemService.adjustStock(id, dto.cnt()));
    }
}
