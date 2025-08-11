package com.hana7.hanaro.item.service;

import static java.time.LocalDateTime.*;

import com.hana7.hanaro.item.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.item.dto.ItemCreateResponseDTO;
import com.hana7.hanaro.item.dto.ItemSearchResponseDTO;
import com.hana7.hanaro.item.dto.ItemUpdateRequestDTO;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.entity.ItemImage;
import com.hana7.hanaro.item.repository.ItemImageRepository;
import com.hana7.hanaro.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    @Value("${upload.path}")
    private String uploadBase;

    @Value("${spring.servlet.multipart.location}")
    private String originBase;

    private final ItemImageRepository itemImageRepository;

    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemCreateResponseDTO createItemWithImages(ItemCreateRequestDTO requestDTO) {

        Item item = itemRepository.save(Item.builder()
            .name(requestDTO.name())
            .price(requestDTO.price())
            .stock(requestDTO.stock())
            .build());


        List<MultipartFile> files = requestDTO.files();
        if (files == null || files.isEmpty()) {
            return ItemCreateResponseDTO.fromCreate(item);
        }

        long total = 0L;
        for (MultipartFile f : files) {
            if (f.isEmpty()) continue;
            if (f.getSize() > 512 * 1024) {
                throw new IllegalArgumentException("파일 당 최대 512KB 초과: " + f.getOriginalFilename());
            }
            total += f.getSize();
        }
        if (total > 3L * 1024 * 1024) {
            throw new IllegalArgumentException("총 업로드 용량 3MB 초과");
        }

        LocalDate today = LocalDate.now();
        String y = String.valueOf(today.getYear());
        String m = String.format("%02d", today.getMonthValue());
        String d = String.format("%02d", today.getDayOfMonth());

        Path originDir = Paths.get(originBase);
        Path uploadDir = Paths.get(uploadBase, y, m, d);
        try {
            Files.createDirectories(originDir);
            Files.createDirectories(uploadDir);
        } catch (Exception e) {
            throw new RuntimeException("업로드 디렉토리 생성 실패", e);
        }

        for (MultipartFile f : files) {
            if (f.isEmpty()) continue;

            String originalName = StringUtils.cleanPath(Objects.requireNonNullElse(f.getOriginalFilename(), "unknown"));
            String ext = StringUtils.getFilenameExtension(originalName);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String saveName = (ext == null || ext.isBlank()) ? uuid : (uuid + "." + ext);

            try (InputStream in = f.getInputStream()) {
                Files.copy(in, originDir.resolve(saveName));
            } catch (Exception e) {
                throw new RuntimeException("원본 저장 실패", e);
            }

            Path uploadPath = uploadDir.resolve(saveName);
            try (InputStream in = f.getInputStream()) {
                Files.copy(in, uploadPath);
            } catch (Exception e) {
                throw new RuntimeException("업로드 저장 실패", e);
            }

            String imgUrl = "/upload/" + y + "/" + m + "/" + d + "/" + saveName;
            ItemImage img = itemImageRepository.save(
                ItemImage.builder()
                    .imgUrl(imgUrl)
                    .item(item)
                    .build()
            );
            item.getItemImages().add(img);
        }

        return ItemCreateResponseDTO.fromCreate(item);
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.findByIdAndDeleteAtIsNull(id)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    @Override
    public List<ItemSearchResponseDTO> searchItemsByName(String name) {
        List<Item> items = (name == null || name.isBlank())
            ? itemRepository.findAllByDeleteAtIsNull()
            : itemRepository.findByDeleteAtIsNullAndNameContainingIgnoreCase(name);

        return items.stream()
            .map(ItemSearchResponseDTO::from)
            .toList();
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAllByDeleteAtIsNull();
    }

    @Override
    @Transactional
    public Item updateItem(ItemUpdateRequestDTO requestDTO,long id) {
        Item item = itemRepository.findByIdAndDeleteAtIsNull(id)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        item.setName(requestDTO.name());
        item.setPrice(requestDTO.price());
        item.setStock(requestDTO.stock());

        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findByIdAndDeleteAtIsNull(id)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        item.setDeleteAt(now());
    }


    @Override
    @Transactional
    public void deleteImage(Long itemId, Long imageId) {
        Item item = itemRepository.findByIdAndDeleteAtIsNull(itemId)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        ItemImage img = itemImageRepository.findById(imageId)
            .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));
        if (!img.getItem().getId().equals(item.getId())) {
            throw new IllegalArgumentException("이 상품의 이미지가 아닙니다.");
        }
        itemImageRepository.delete(img);
    }

    @Override
    @Transactional
    public Item adjustStock(Long id, int cnt) {
        Item item = itemRepository.findByIdAndDeleteAtIsNull(id)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        if (cnt < 0) throw new IllegalArgumentException("재고는 음수가 될 수 없습니다.");
        item.setStock(cnt);
        return item;
    }
}
