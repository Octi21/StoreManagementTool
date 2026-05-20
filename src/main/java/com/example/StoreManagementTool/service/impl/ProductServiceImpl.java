package com.example.StoreManagementTool.service.impl;

import com.example.StoreManagementTool.dto.request.CreateProductRequest;
import com.example.StoreManagementTool.dto.request.UpdateProductRequest;
import com.example.StoreManagementTool.dto.request.UpdateStockRequest;
import com.example.StoreManagementTool.dto.response.ProductResponse;
import com.example.StoreManagementTool.entity.Category;
import com.example.StoreManagementTool.entity.Product;
import com.example.StoreManagementTool.exception.ResourceNotFoundException;
import com.example.StoreManagementTool.mapper.ProductMapper;
import com.example.StoreManagementTool.repository.CategoryRepository;
import com.example.StoreManagementTool.repository.ProductRepository;
import com.example.StoreManagementTool.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl  implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);


    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;


    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,CategoryRepository categoryRepository, ProductMapper productMapper){
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse create(CreateProductRequest request){
        Category category = resolveCategory(request.categoryId());

        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                request.stockQuantity(),
                category
        );

        Product saved = productRepository.save(product);

        log.info("Created product id={} name={}", saved.getId(), saved.getName());
        return productMapper.toResponse(saved);

    }

    public List<ProductResponse> findAll(){
        List<Product> products = productRepository.findAll();

        return products.stream().map(productMapper::toResponse).toList();
    }

    public List<ProductResponse> findAllByCategoryName(String categoryName){
        if (!categoryRepository.existsByName(categoryName)) {
            throw new EntityNotFoundException(
                    "Category not found with name: " + categoryName);
        }

        List<Product> products = productRepository.findByCategoryName(categoryName);

        return products.stream().map(productMapper::toResponse).toList();
    }

    public ProductResponse findById(Long id) {
        Product product = getProductOrThrow(id);
        return productMapper.toResponse(product);
    }

    public ProductResponse updateStock(Long id, UpdateStockRequest request) {
        Product product = getProductOrThrow(id);
        product.setStockQuantity(request.stockQuantity());
        log.info("Updated stock for product id={} to {}", id, request.stockQuantity());
        return productMapper.toResponse(product);
    }

    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product product = getProductOrThrow(id);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(resolveCategory(request.categoryId()));
        log.info("Updated product id={}", id);
        return productMapper.toResponse(product);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Product", id);
        }
        productRepository.deleteById(id);
        log.info("Deleted product id={}", id);
    }


    private Product getProductOrThrow(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Product", id));
    }

    private Category resolveCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> ResourceNotFoundException.of("Category", categoryId));
    }


}
