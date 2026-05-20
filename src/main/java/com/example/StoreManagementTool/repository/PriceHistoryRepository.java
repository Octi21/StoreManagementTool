package com.example.StoreManagementTool.repository;

import com.example.StoreManagementTool.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory,Long> {
    List<PriceHistory> findByProductIdOrderByChangedAtDesc(Long productId);
}
