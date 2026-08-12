package com.example.assettracker.dtos;

import com.example.assettracker.domain.AssetStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateAssetRequest(
        @NotBlank(message = "Asset name cannot be left blank.")
        String name,

        @NotNull(message = "Status is required.")
        AssetStatus status,

        @NotNull(message = "Purchase cost is required.")
        @Positive(message = "Purchase cost amount must be positive.")
        BigDecimal purchaseCost,

        Long categoryId
) {
}
