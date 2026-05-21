package com.example.StoreManagementTool.repository;

import com.example.StoreManagementTool.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryName(String name,Pageable pageable);
//    List<Product> findByCategoryId(Long categoryId);
}