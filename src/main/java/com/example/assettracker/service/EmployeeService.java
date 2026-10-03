package com.example.assettracker.service;

import com.example.assettracker.domain.EmployeeEntity;
import com.example.assettracker.domain.EmployeeStatus;
import com.example.assettracker.dtos.CreateEmployeeRequest;
import com.example.assettracker.dtos.EmployeeResponse;
import com.example.assettracker.dtos.UpdateEmployeeRequest;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(CreateEmployeeRequest request);
    EmployeeResponse getEmployeeById(Long id);
    EmployeeResponse getByEmployeeId(String employeeId);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request);
    void deleteEmployee(Long id);
    List<EmployeeResponse> getActiveEmployees();
}
