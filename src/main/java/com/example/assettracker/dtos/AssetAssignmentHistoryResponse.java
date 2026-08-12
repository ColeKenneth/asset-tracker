package com.example.assettracker.dtos;

import java.time.LocalDateTime;

public record AssetAssignmentHistoryResponse(
        Long id,
        Long assetId,
        String assetTag,
        Long employeeId,
        String employeeName,
        LocalDateTime assignedAt,
        LocalDateTime returnedAt
) {
}
