package com.souvik.ems.repository;

import com.souvik.ems.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmail(String email);
    Boolean existsByEmail(String email);
    Page<Employee> findByFirstNameContainsIgnoreCase(String firstName, Pageable pageable);
    Page<Employee> findByLastNameContainsIgnoreCase(String lastName, Pageable pageable);
    Page<Employee> findByEmailContainsIgnoreCase(String firstName, Pageable pageable);
    Page<Employee> findByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(String firstName, String lastName, Pageable pageable);
    Page<Employee> findByFirstNameContainsIgnoreCaseAndEmailContainsIgnoreCase(String firstName, String lastName, Pageable pageable);
    Page<Employee> findByLastNameContainsIgnoreCaseAndEmailContainsIgnoreCase(String lastName, String email, Pageable pageable);
    Page<Employee> findByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndEmailContainsIgnoreCase(String firstName, String lastName, String email, Pageable pageable);

}