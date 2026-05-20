package com.example.StoreManagementTool.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceHistoryResponse(
        Long id,
        Long productId,
        BigDecimal oldPrice,
        BigDecimal newPrice,
        Instant changedAt
) {
}