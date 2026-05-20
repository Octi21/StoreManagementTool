package com.example.StoreManagementTool.service.impl;

import com.example.StoreManagementTool.dto.request.CreateCategoryRequest;
import com.example.StoreManagementTool.dto.response.CategoryResponse;
import com.example.StoreManagementTool.entity.Category;
import com.example.StoreManagementTool.entity.Product;
import com.example.StoreManagementTool.exception.DuplicateResourceException;
import com.example.StoreManagementTool.exception.ResourceNotFoundException;
import com.example.StoreManagementTool.mapper.CategoryMapper;
import com.example.StoreManagementTool.mapper.ProductMapper;
import com.example.StoreManagementTool.repository.CategoryRepository;
import com.example.StoreManagementTool.service.CategoryService;
import com.example.StoreManagementTool.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;



    public CategoryServiceImpl(CategoryRepository categoryRepository,CategoryMapper categoryMapper){
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryResponse> findAll(){
        return categoryRepository.findAll().stream().map(categoryMapper::toResponse).toList();
    }

    public CategoryResponse findById(Long id){
        Category category = getCategoryOrThrow(id);
        return  categoryMapper.toResponse(category);

    }

    public CategoryResponse create(CreateCategoryRequest request){
        if (categoryRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Category with name " + request.name() + " already exists");
        }
        Category category = new Category(request.name(), request.description());
        Category saved = categoryRepository.save(category);
        log.info("Created category id={} name={}", saved.getId(), saved.getName());
        return categoryMapper.toResponse(saved);
    }

    public CategoryResponse update(Long id, CreateCategoryRequest request){
        Category category = getCategoryOrThrow(id);

        if (!category.getName().equals(request.name()) && categoryRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Category with name " + request.name() + " already exists");
        }

        category.setName(request.name());
        category.setDescription(request.description());
        log.info("Updated category id={}", id);
        return categoryMapper.toResponse(category);
    }

    public void delete(Long id){
        if (!categoryRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Category", id);
        }
        categoryRepository.deleteById(id);
        log.info("Deleted category id={}", id);
    }

    private Category getCategoryOrThrow(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Category", id));
    }
}
