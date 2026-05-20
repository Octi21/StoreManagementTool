package com.example.StoreManagementTool.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ChangePriceRequest(
        @NotNull @DecimalMin(value = "0.01", message = "Price must be greater than 0") BigDecimal newPrice
) {
}

