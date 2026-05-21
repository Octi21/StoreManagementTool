package com.example.StoreManagementTool.service.impl;

import com.example.StoreManagementTool.dto.request.ChangePriceRequest;
import com.example.StoreManagementTool.dto.request.CreateProductRequest;
import com.example.StoreManagementTool.dto.request.UpdateProductRequest;
import com.example.StoreManagementTool.dto.request.UpdateStockRequest;
import com.example.StoreManagementTool.dto.response.PriceHistoryResponse;
import com.example.StoreManagementTool.dto.response.ProductResponse;
import com.example.StoreManagementTool.entity.Category;
import com.example.StoreManagementTool.entity.PriceHistory;
import com.example.StoreManagementTool.entity.Product;
import com.example.StoreManagementTool.exception.ResourceNotFoundException;
import com.example.StoreManagementTool.mapper.PriceHistoryMapper;
import com.example.StoreManagementTool.mapper.ProductMapper;
import com.example.StoreManagementTool.repository.CategoryRepository;
import com.example.StoreManagementTool.repository.PriceHistoryRepository;
import com.example.StoreManagementTool.repository.ProductRepository;
import com.example.StoreManagementTool.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Service
@Transactional
public class ProductServiceImpl  implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);


    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final PriceHistoryRepository priceHistoryRepository;


    private final ProductMapper productMapper;

    private final PriceHistoryMapper priceHistoryMapper;


    public ProductServiceImpl(ProductRepository productRepository,CategoryRepository categoryRepository, PriceHistoryRepository priceHistoryRepository, ProductMapper productMapper,PriceHistoryMapper priceHistoryMapper){
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.priceHistoryRepository = priceHistoryRepository;
        this.priceHistoryMapper = priceHistoryMapper;
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
    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable){
        Page<Product> page = productRepository.findAll(pageable);

        return page.map(productMapper::toResponse);
    }
    @Transactional(readOnly = true)
    public Page<ProductResponse> findAllByCategoryName(String categoryName,Pageable pageable){
        if (!categoryRepository.existsByName(categoryName)) {
            throw new EntityNotFoundException(
                    "Category not found with name: " + categoryName);
        }

        Page<Product> page = productRepository.findByCategoryName(categoryName,pageable);

        return page.map(productMapper::toResponse);
    }
    @Transactional(readOnly = true)
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
    public ProductResponse changePrice(Long id, ChangePriceRequest request){
        Product product = getProductOrThrow(id);
        BigDecimal oldPrice = product.getPrice();
        BigDecimal newPrice = request.newPrice();

        if (oldPrice.compareTo(newPrice) == 0) {
            log.debug("Price change requested for product id={} but value is unchanged", id);
            return productMapper.toResponse(product);
        }

        product.setPrice(newPrice);
        priceHistoryRepository.save(new PriceHistory(product,oldPrice,newPrice));
        log.info("Changed price for product id={} from {} to {}", id, oldPrice, newPrice);

        return productMapper.toResponse(product);
    }
    @Transactional(readOnly = true)
    public List<PriceHistoryResponse> getPriceHistory(Long productId){
        if (!productRepository.existsById(productId)) {
            throw ResourceNotFoundException.of("Product", productId);
        }
        return priceHistoryRepository.findByProductIdOrderByChangedAtDesc(productId)
                .stream()
                .map(priceHistoryMapper::toResponse)
                .toList();

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
