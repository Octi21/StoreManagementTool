package com.example.StoreManagementTool.service.impl;

import com.example.StoreManagementTool.dto.request.ChangePriceRequest;
import com.example.StoreManagementTool.dto.request.CreateProductRequest;
import com.example.StoreManagementTool.dto.request.UpdateProductRequest;
import com.example.StoreManagementTool.dto.request.UpdateStockRequest;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private PriceHistoryRepository priceHistoryRepository;

    private final ProductMapper productMapper = new ProductMapper();
    private final PriceHistoryMapper priceHistoryMapper = new PriceHistoryMapper();

    private ProductService productService;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(
                productRepository,
                categoryRepository,
                priceHistoryRepository,
                productMapper,
                priceHistoryMapper
        );

        category = new Category("Electronics", "Gadgets");
        category.setId(1L);

        product = new Product("Laptop", "Powerful laptop", new BigDecimal("1500.00"), 10, category);
        product.setId(100L);
    }

    // ----- findById -----

    @Test
    @DisplayName("findById returns the product when it exists")
    void findById_returnsProduct() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.findById(100L);

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.name()).isEqualTo("Laptop");
        assertThat(response.price()).isEqualByComparingTo("1500.00");
    }

    @Test
    @DisplayName("findById throws ResourceNotFoundException when product is missing")
    void findById_throwsWhenMissing() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product")
                .hasMessageContaining("999");
    }

    // ----- create -----

    @Test
    @DisplayName("create persists a product and resolves the category")
    void create_persistsProduct() {
        CreateProductRequest request = new CreateProductRequest(
                "Phone", "Smartphone", new BigDecimal("799.99"), 50, 1L
        );

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(200L);
            return p;
        });

        ProductResponse response = productService.create(request);

        assertThat(response.id()).isEqualTo(200L);
        assertThat(response.name()).isEqualTo("Phone");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("create with null categoryId saves the product without a category")
    void create_allowsNullCategory() {
        CreateProductRequest request = new CreateProductRequest(
                "Headphones", "Wireless", new BigDecimal("99.99"), 20, null
        );

        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(300L);
            return p;
        });

        ProductResponse response = productService.create(request);

        assertThat(response.id()).isEqualTo(300L);
        // categoryRepository should not be touched when id is null
        verify(categoryRepository, never()).findById(any());
    }

    @Test
    @DisplayName("create throws when category does not exist")
    void create_throwsWhenCategoryMissing() {
        CreateProductRequest request = new CreateProductRequest(
                "Phone", "Smartphone", new BigDecimal("799.99"), 50, 99L
        );

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productRepository, never()).save(any());
    }

    // ----- update -----

    @Test
    @DisplayName("update changes name, description, and category")
    void update_updatesFields() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        UpdateProductRequest request = new UpdateProductRequest("Laptop Pro", "Renamed", 1L);
        ProductResponse response = productService.update(100L, request);

        assertThat(response.name()).isEqualTo("Laptop Pro");
        assertThat(response.description()).isEqualTo("Renamed");
    }

    // ----- updateStock -----

    @Test
    @DisplayName("updateStock changes the stock quantity")
    void updateStock_updatesValue() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.updateStock(100L, new UpdateStockRequest(42));

        assertThat(response.stockQuantity()).isEqualTo(42);
    }

    // ----- changePrice -----

    @Test
    @DisplayName("changePrice updates the price and records a history row")
    void changePrice_recordsHistory() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        ChangePriceRequest request = new ChangePriceRequest(new BigDecimal("1299.99"));
        ProductResponse response = productService.changePrice(100L, request);

        assertThat(response.price()).isEqualByComparingTo("1299.99");

        ArgumentCaptor<PriceHistory> captor = ArgumentCaptor.forClass(PriceHistory.class);
        verify(priceHistoryRepository).save(captor.capture());

        PriceHistory saved = captor.getValue();
        assertThat(saved.getOldPrice()).isEqualByComparingTo("1500.00");
        assertThat(saved.getNewPrice()).isEqualByComparingTo("1299.99");
    }

    @Test
    @DisplayName("changePrice does NOT record history when the price is unchanged")
    void changePrice_skipsHistoryWhenUnchanged() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        productService.changePrice(100L, new ChangePriceRequest(new BigDecimal("1500.00")));

        verify(priceHistoryRepository, never()).save(any());
    }

    // ----- findAll / findAllByCategoryName -----

    @Test
    @DisplayName("findAll returns a paginated list of products")
    void findAll_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);
        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<ProductResponse> result = productService.findAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Laptop");
    }

    @Test
    @DisplayName("findAllByCategoryName returns products for that category")
    void findAllByCategoryName_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);
        when(categoryRepository.existsByName("Electronics")).thenReturn(true);
        when(productRepository.findByCategoryName("Electronics", pageable)).thenReturn(page);

        Page<ProductResponse> result = productService.findAllByCategoryName("Electronics", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Laptop");
    }

    @Test
    @DisplayName("findAllByCategoryName throws EntityNotFoundException when the category does not exist")
    void findAllByCategoryName_throwsWhenCategoryMissing() {
        Pageable pageable = PageRequest.of(0, 10);
        when(categoryRepository.existsByName("Nope")).thenReturn(false);

        assertThatThrownBy(() -> productService.findAllByCategoryName("Nope", pageable))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Nope");

        verify(productRepository, never()).findByCategoryName(eq("Nope"), any());
    }

    // ----- delete -----

    @Test
    @DisplayName("delete removes the product when it exists")
    void delete_removesProduct() {
        when(productRepository.existsById(100L)).thenReturn(true);

        productService.delete(100L);

        verify(productRepository).deleteById(100L);
    }

    @Test
    @DisplayName("delete throws when the product does not exist")
    void delete_throwsWhenMissing() {
        when(productRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> productService.delete(999L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productRepository, never()).deleteById(any());
    }
}