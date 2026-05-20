package com.example.StoreManagementTool.mapper;

import com.example.StoreManagementTool.dto.response.CategoryResponse;
import com.example.StoreManagementTool.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
