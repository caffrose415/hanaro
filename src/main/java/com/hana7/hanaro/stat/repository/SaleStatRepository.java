package com.hana7.hanaro.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana7.hanaro.stat.entity.SaleStat;

public interface SaleStatRepository extends JpaRepository<SaleStat, Integer> {
}
