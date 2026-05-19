package com.example.StoreManagementTool.service.impl;

import com.example.StoreManagementTool.dto.request.CreateProductRequest;
import com.example.StoreManagementTool.dto.response.ProductResponse;
import com.example.StoreManagementTool.entity.Product;
import com.example.StoreManagementTool.mapper.ProductMapper;
import com.example.StoreManagementTool.repository.ProductRepository;
import com.example.StoreManagementTool.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl  implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);


    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper){
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    public ProductResponse create(CreateProductRequest request){
        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                request.stockQuantity()
        );

        Product saved = productRepository.save(product);

        log.info("Created product id={} name={}", saved.getId(), saved.getName());
        return productMapper.toResponse(saved);

    }

    public List<ProductResponse> findAll(){
        List<Product> products = productRepository.findAll();

        return products.stream().map(productMapper::toResponse).toList();
    }
}
