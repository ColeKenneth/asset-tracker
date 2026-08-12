package com.example.assettracker.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateAssetRequest(
        @NotBlank(message = "Asset tag cannot be left blank.")
        @Size(min = 3, max = 50, message = "Asset tag must be between 3 to 50 characters long.")
        String assetTag,

        @NotBlank(message = "Asset name cannot be left blank.")
        String name,

        @NotBlank(message = "Serial number cannot be left blank.")
        @Pattern(regexp = "^[A-Z0-9]{2,10}-[A-Z0-9]{4,15}$",
        message = "Serial number must match format 'PREFIX-NUMBER' (e.g., 'SN-9948102'")
        String serialNumber,

        @NotNull(message = "Purchase cost cannot be blank.")
        @Positive(message = "Purchase cost amount must be positive.")
        BigDecimal purchaseCost,

        @NotNull(message = "Purchase date cannot be left blank.")
        @PastOrPresent(message = "Purchase date cannot happen in the future.")
        LocalDate purchaseDate,

        Long categoryId
) {
        public CreateAssetRequest {
                if (assetTag != null) {
                        assetTag = assetTag.trim().toUpperCase();
                }

                if (name != null) {
                        name = name.trim();
                }
                if (serialNumber != null) {
                        serialNumber = serialNumber.trim().toUpperCase();
                }
        }
}
