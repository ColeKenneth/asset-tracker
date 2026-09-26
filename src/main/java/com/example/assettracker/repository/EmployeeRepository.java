package com.example.assettracker.repository;

import com.example.assettracker.domain.EmployeeEntity;
import com.example.assettracker.domain.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    Optional<EmployeeEntity> findByEmployeeId(String employeeId);
    List<EmployeeEntity> findAllByStatus(EmployeeStatus status);
}
