package com.example.assettracker.service.impl;

import com.example.assettracker.domain.EmployeeEntity;
import com.example.assettracker.domain.EmployeeStatus;
import com.example.assettracker.dtos.CreateEmployeeRequest;
import com.example.assettracker.dtos.EmployeeResponse;
import com.example.assettracker.exception.ResourceNotFoundException;
import com.example.assettracker.repository.EmployeeRepository;
import com.example.assettracker.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        if (employeeRepository.findByEmployeeId(request.employeeId()).isPresent()) {
            log.warn("Employee already exists: {}", request.employeeId());
            throw new IllegalArgumentException("Employee ID already exists: " + request.employeeId());
        }

        var employee = EmployeeEntity.builder()
                .employeeId(request.employeeId())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .department(request.department())
                .status(EmployeeStatus.ACTIVE)
                .build();

        var saved = employeeRepository.save(employee);
        return mapToResponse(saved);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        return mapToResponse(employee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private EmployeeResponse mapToResponse(EmployeeEntity entity) {
        return new EmployeeResponse(
                entity.getId(),
                entity.getEmployeeId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getDepartment(),
                entity.getStatus()
        );
    }
}
