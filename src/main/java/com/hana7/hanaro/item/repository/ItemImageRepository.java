package com.hana7.hanaro.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.hanaro.item.entity.ItemImage;

public interface ItemImageRepository extends JpaRepository<ItemImage,Long> {
}
