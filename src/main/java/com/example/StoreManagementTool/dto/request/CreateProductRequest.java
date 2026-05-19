package com.example.StoreManagementTool.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 500) String description,
        @NotNull @DecimalMin(value = "0.01", message = "Price must be greater than 0") BigDecimal price,
        @NotNull @Min(value = 0, message = "Stock cannot be negative") Integer stockQuantity
) {
}
