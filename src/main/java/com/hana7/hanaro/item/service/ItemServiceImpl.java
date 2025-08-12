package com.hana7.hanaro.item.service;

import static java.time.LocalDateTime.*;

import com.hana7.hanaro.common.exception.BusinessException;
import com.hana7.hanaro.common.exception.ErrorCode;
import com.hana7.hanaro.item.dto.ItemCreateRequestDTO;
import com.hana7.hanaro.item.dto.ItemCreateResponseDTO;
import com.hana7.hanaro.item.dto.ItemSearchResponseDTO;
import com.hana7.hanaro.item.dto.ItemUpdateRequestDTO;
import com.hana7.hanaro.item.entity.Item;
import com.hana7.hanaro.item.entity.ItemImage;
import com.hana7.hanaro.item.repository.ItemImageRepository;
import com.hana7.hanaro.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
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
        log.info("Service 관리자 : 아이템 등록");
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
                throw new BusinessException(ErrorCode.PAYLOAD_TOO_LARGE);
            }
            total += f.getSize();
        }
        if (total > 3L * 1024 * 1024) {
            throw new BusinessException(ErrorCode.PAYLOAD_TOO_LARGE);
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
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
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
                throw new BusinessException(ErrorCode.INTERNAL_ERROR);
            }

            Path uploadPath = uploadDir.resolve(saveName);
            try (InputStream in = f.getInputStream()) {
                Files.copy(in, uploadPath);
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.INTERNAL_ERROR);
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
        log.info("Service 관리자 : 아이템 id값으로 검색 id={}",id);
        return itemRepository.findByIdAndDeleteAtIsNull(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT));
    }

    @Override
    public List<ItemSearchResponseDTO> searchItemsByName(String name) {
        log.info("Service 관리자 : 이름으로 검색 name={}",name);
        List<Item> items = (name == null || name.isBlank())
            ? itemRepository.findAllByDeleteAtIsNull()
            : itemRepository.findByDeleteAtIsNullAndNameContainingIgnoreCase(name);

        return items.stream()
            .map(ItemSearchResponseDTO::from)
            .toList();
    }

    @Override
    public List<Item> getAllItems() {
        log.info("Service 관리자 : 아이템 전체 검색");
        return itemRepository.findAllByDeleteAtIsNull();
    }

    @Override
    @Transactional
    public Item updateItem(ItemUpdateRequestDTO requestDTO,long id) {
        log.info("Service 관리자 : 아이템 수정 id={}",id);
        Item item = itemRepository.findByIdAndDeleteAtIsNull(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT));

        item.setName(requestDTO.name());
        item.setPrice(requestDTO.price());
        item.setStock(requestDTO.stock());

        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        log.info("Service 관리자 : 아이템 삭제 id={}",id);
        Item item = itemRepository.findByIdAndDeleteAtIsNull(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT));
        item.setDeleteAt(now());
    }


    @Override
    @Transactional
    public void deleteImage(Long itemId, Long imageId) {
        log.info("Service 관리자 : 이미지 삭제 itemId={}, imageId={}",itemId,imageId);
        Item item = itemRepository.findByIdAndDeleteAtIsNull(itemId)
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT));
        ItemImage img = itemImageRepository.findById(imageId)
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT));
        if (!img.getItem().getId().equals(item.getId())) {
            throw new IllegalArgumentException("이 상품의 이미지가 아닙니다.");
        }
        itemImageRepository.delete(img);
    }

    @Override
    @Transactional
    public Item adjustStock(Long id, int cnt) {
        log.info("Service 관리자 : 아이템 수량 조정 id={},cnt={}",id,cnt);
        Item item = itemRepository.findByIdAndDeleteAtIsNull(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT));
        if (cnt < 0) throw new BusinessException(ErrorCode.INVALID_INPUT);
        item.setStock(cnt);
        return item;
    }
}
