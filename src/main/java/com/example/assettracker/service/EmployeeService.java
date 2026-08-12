package com.example.assettracker.service;

import com.example.assettracker.dtos.CreateEmployeeRequest;
import com.example.assettracker.dtos.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(CreateEmployeeRequest request);
    EmployeeResponse getEmployeeById(Long id);
    List<EmployeeResponse> getAllEmployees();
}
