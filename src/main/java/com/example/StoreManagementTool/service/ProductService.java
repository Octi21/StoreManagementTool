package com.example.StoreManagementTool.service;

import com.example.StoreManagementTool.dto.request.ChangePriceRequest;
import com.example.StoreManagementTool.dto.request.CreateProductRequest;
import com.example.StoreManagementTool.dto.request.UpdateProductRequest;
import com.example.StoreManagementTool.dto.request.UpdateStockRequest;
import com.example.StoreManagementTool.dto.response.PriceHistoryResponse;
import com.example.StoreManagementTool.dto.response.ProductResponse;
import com.example.StoreManagementTool.entity.Product;
import com.example.StoreManagementTool.exception.ResourceNotFoundException;

import java.util.List;

public interface ProductService {
    public ProductResponse create(CreateProductRequest createProductRequest);

    public List<ProductResponse> findAll();
    public List<ProductResponse> findAllByCategoryName(String categoryName);


    public ProductResponse findById(Long id);

    public ProductResponse updateStock(Long id, UpdateStockRequest request);

    public ProductResponse update(Long id, UpdateProductRequest request);

    public ProductResponse changePrice(Long id, ChangePriceRequest request);
    public List<PriceHistoryResponse> getPriceHistory(Long productId);


    public void delete(Long id);


}
