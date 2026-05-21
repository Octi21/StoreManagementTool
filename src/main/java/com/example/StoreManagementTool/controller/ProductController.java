package com.example.StoreManagementTool.controller;

import com.example.StoreManagementTool.dto.request.ChangePriceRequest;
import com.example.StoreManagementTool.dto.request.CreateProductRequest;
import com.example.StoreManagementTool.dto.request.UpdateProductRequest;
import com.example.StoreManagementTool.dto.request.UpdateStockRequest;
import com.example.StoreManagementTool.dto.response.PriceHistoryResponse;
import com.example.StoreManagementTool.dto.response.ProductResponse;
import com.example.StoreManagementTool.repository.ProductRepository;
import com.example.StoreManagementTool.service.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    @PageableAsQueryParam
    public Page<ProductResponse> getAll(@RequestParam(required = false) String categoryName,
                                        @Parameter(hidden = true)
                                        @PageableDefault(size = 20, sort = "name") Pageable pageable){
        if(categoryName != null){
            return  productService.findAllByCategoryName(categoryName,pageable);
        }
        return productService.findAll(pageable);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request){
        ProductResponse created = productService.create(request);
        return ResponseEntity.created(URI.create("/api/products/"+created.id())).body(created);
    }

    @GetMapping("{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        return productService.update(id, request);
    }

    @PatchMapping("/{id}/stock")
    public ProductResponse updateStock(@PathVariable Long id, @Valid @RequestBody UpdateStockRequest request) {
        return productService.updateStock(id, request);
    }


    @PatchMapping("/{id}/price")
    public ProductResponse changePrice(@PathVariable Long id,
                                       @Valid @RequestBody ChangePriceRequest request) {
        return productService.changePrice(id, request);
    }

    @GetMapping("/{id}/price-history")
    public List<PriceHistoryResponse> priceHistory(@PathVariable Long id) {
        return productService.getPriceHistory(id);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

}
