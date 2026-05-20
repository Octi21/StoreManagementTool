package com.example.StoreManagementTool.service;

import com.example.StoreManagementTool.dto.request.CreateCategoryRequest;
import com.example.StoreManagementTool.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    public List<CategoryResponse> findAll();

    public CategoryResponse findById(Long id);

    public CategoryResponse create(CreateCategoryRequest request);

    public CategoryResponse update(Long id, CreateCategoryRequest request);

    public void delete(Long id);
}
