package com.example.assettracker.dtos;

import com.example.assettracker.domain.EmployeeStatus;

public record EmployeeResponse(
        Long id,
        String employeeId,
        String firstName,
        String lastName,
        String email,
        String department,
        EmployeeStatus status
) {
}
