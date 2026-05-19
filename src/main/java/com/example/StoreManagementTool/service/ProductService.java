package com.example.StoreManagementTool.service;

import com.example.StoreManagementTool.dto.request.CreateProductRequest;
import com.example.StoreManagementTool.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    public ProductResponse create(CreateProductRequest createProductRequest);

    public List<ProductResponse> findAll();

}
