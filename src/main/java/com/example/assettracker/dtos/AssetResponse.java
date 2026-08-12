package com.example.assettracker.dtos;

import com.example.assettracker.domain.AssetStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetResponse(
        Long id,
        String assetTag,
        String name,
        String serialNumber,
        AssetStatus status,
        BigDecimal purchaseCost,
        LocalDate purchaseDate,
        String categoryName,
        String assignedEmployeeName
) {
}
