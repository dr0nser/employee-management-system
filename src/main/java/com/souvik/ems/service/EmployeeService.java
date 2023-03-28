package com.souvik.ems.service;

import com.souvik.ems.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface EmployeeService {
    Long getEmployeeCount();
    Page<Employee> getAllEmployees(int pageNo, int pageSize, String sortField, String sortOrder);
    void saveEmployee(Employee employee);
    Employee getEmployee(UUID id);
    void deleteEmployee(UUID id);
    Page<Employee> search(String firstName, String lastName, String email, int pageNo, int pageSize, String sortField, String sortOrder);
    Optional<Employee> findByEmail(String email);
    Boolean existsByEmail(String email);
    String getLoggedInUserUsername();
}