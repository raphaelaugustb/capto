package com.leah.cstock.io.repository;

import com.leah.cstock.io.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
