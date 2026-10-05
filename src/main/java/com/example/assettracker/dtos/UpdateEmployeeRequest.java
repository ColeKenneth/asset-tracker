package com.example.assettracker.dtos;

import com.example.assettracker.domain.EmployeeStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateEmployeeRequest(
        @NotBlank(message = "Employee ID is required.")
        String employeeId,

        @NotBlank(message = "First name is required.")
        String firstName,

        @NotBlank(message = "Last name is required.")
        String lastName,

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        String email,

        String department,

        @NotNull(message = "Employee status is required.")
        EmployeeStatus status
) {
}
