package com.example.assettracker.dtos;

import jakarta.validation.constraints.NotNull;

public record AssignAssetRequest(
        @NotNull(message = "Employee ID is required.")
        Long employeeId
) {
}
