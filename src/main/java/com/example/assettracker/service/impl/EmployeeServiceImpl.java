package com.example.assettracker.service.impl;

import com.example.assettracker.domain.EmployeeEntity;
import com.example.assettracker.domain.EmployeeStatus;
import com.example.assettracker.dtos.CreateEmployeeRequest;
import com.example.assettracker.dtos.EmployeeResponse;
import com.example.assettracker.dtos.UpdateEmployeeRequest;
import com.example.assettracker.exception.ResourceNotFoundException;
import com.example.assettracker.repository.EmployeeRepository;
import com.example.assettracker.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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

        try {
            employeeRepository.save(employee);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Employee already exists: " + request.employeeId());
        }
        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponse getByEmployeeId(String employeeId) {
        var employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with employee ID: " + employeeId));

        return mapToResponse(employee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request) {
        var entity =  employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        if (!entity.getEmployeeId().equals(request.employeeId())) {
            if (employeeRepository.findByEmployeeId(request.employeeId()).isPresent()) {
                log.warn("Attempt to update employee with ID of {}", request.employeeId());
                throw new IllegalArgumentException("Employee ID already exists: " + request.employeeId());
            }
            entity.setEmployeeId(request.employeeId());
        }

        if (!entity.getEmail().equalsIgnoreCase(request.email())) {
            if (employeeRepository.findByEmail(request.email()).isPresent()) {
                log.warn("Attempt to update employee with email: {}", request.email());
                throw new IllegalArgumentException("Email already exists: " + request.email());
            }
            entity.setEmail(request.email());
        }

        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());
        entity.setDepartment(request.department());
        entity.setStatus(request.status());

        log.info("Updated employee with ID: {}", id);
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        var entity = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        if (entity.getStatus() == EmployeeStatus.ACTIVE) {
            throw new IllegalStateException("Cannot delete an employee who is active.");
        }

        employeeRepository.delete(entity);
        log.info("Deleted employee with ID: {}", id);
    }

    @Override
    public List<EmployeeResponse> getActiveEmployees() {
        return employeeRepository.findAllByStatus(EmployeeStatus.ACTIVE).stream()
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
