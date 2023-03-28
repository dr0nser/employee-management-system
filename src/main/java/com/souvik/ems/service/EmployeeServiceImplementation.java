package com.souvik.ems.service;

import com.souvik.ems.model.Employee;
import com.souvik.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeServiceImplementation implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Long getEmployeeCount() {
        return employeeRepository.count();
    }

    @Override
    public Page<Employee> getAllEmployees(int pageNo, int pageSize, String sortField, String sortOrder) {

        // if current order is "asc" then change it to "desc" and vice-versa
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        return employeeRepository.findAll(pageable);
    }

    @Override
    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployee(UUID id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElse(null);
    }

    @Override
    public void deleteEmployee(UUID id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Page<Employee> search(String firstName, String lastName, String email, int pageNo, int pageSize, String sortField, String sortOrder) {

        // if current order is "asc" then change it to "desc" and vice-versa
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        if (firstName.length() > 0 && lastName.length() > 0 && email.length() > 0)
            return employeeRepository.findByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndEmailContainsIgnoreCase(firstName, lastName, email, pageable);
        else if (firstName.length() > 0 && lastName.length() > 0)
            return employeeRepository.findByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(firstName, lastName, pageable);
        else if (firstName.length() > 0 && email.length() > 0)
            return employeeRepository.findByFirstNameContainsIgnoreCaseAndEmailContainsIgnoreCase(firstName, email, pageable);
        else if (lastName.length() > 0 && email.length() > 0)
            return employeeRepository.findByLastNameContainsIgnoreCaseAndEmailContainsIgnoreCase(lastName, email, pageable);
        else if (firstName.length() > 0)
            return employeeRepository.findByFirstNameContainsIgnoreCase(firstName, pageable);
        else if (lastName.length() > 0)
            return employeeRepository.findByLastNameContainsIgnoreCase(lastName, pageable);
        else if (email.length() > 0)
            return employeeRepository.findByEmailContainsIgnoreCase(email, pageable);
        // dummy return, condition checked at controller
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public String getLoggedInUserUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String loggedInUserEmail = userDetails.getUsername();
        Employee loggedInUserDetails = employeeRepository.findByEmail(loggedInUserEmail).get();
        return loggedInUserDetails.getFirstName() + " " + loggedInUserDetails.getLastName();
    }
}