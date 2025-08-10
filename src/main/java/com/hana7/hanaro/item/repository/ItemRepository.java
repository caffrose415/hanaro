package com.hana7.hanaro.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.hanaro.item.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long> {
    List<Item> findByNameContainingIgnoreCase(String name);
}
