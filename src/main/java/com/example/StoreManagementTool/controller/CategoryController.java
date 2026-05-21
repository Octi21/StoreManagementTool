package com.example.StoreManagementTool.controller;

import com.example.StoreManagementTool.dto.request.CreateCategoryRequest;
import com.example.StoreManagementTool.dto.response.CategoryResponse;
import com.example.StoreManagementTool.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponse> list() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse created = categoryService.create(request);
        return ResponseEntity.created(URI.create("/api/categories/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CreateCategoryRequest request){
        return categoryService.update(id,request);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }

}
