package com.example.StoreManagementTool.mapper;

import com.example.StoreManagementTool.dto.response.PriceHistoryResponse;
import com.example.StoreManagementTool.entity.PriceHistory;
import org.springframework.stereotype.Component;

@Component
public class PriceHistoryMapper {

    public PriceHistoryResponse toResponse(PriceHistory history) {
        return new PriceHistoryResponse(
                history.getId(),
                history.getProduct().getId(),
                history.getOldPrice(),
                history.getNewPrice(),
                history.getChangedAt()
        );
    }
}