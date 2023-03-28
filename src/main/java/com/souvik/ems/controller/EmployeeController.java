package com.souvik.ems.controller;

import com.souvik.ems.dto.EmployeeDTO;
import com.souvik.ems.model.Employee;
import com.souvik.ems.dto.SearchQuery;
import com.souvik.ems.model.Role;
import com.souvik.ems.service.EmployeeService;
import com.souvik.ems.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@AllArgsConstructor
@Controller
public class EmployeeController {

    private static final int PAGE_SIZE = 10;

    private EmployeeService employeeService;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        return getAllEmployees(model, 1, "firstName", "asc");
    }

    @GetMapping("/{pageNo}")
    public String getAllEmployees(Model model,
                                  @PathVariable(value = "pageNo") int pageNo,
                                  @RequestParam(name = "sortField") String sortField,
                                  @RequestParam(name = "sortOrder") String sortOrder) {

        Page<Employee> page = employeeService.getAllEmployees(pageNo, PAGE_SIZE, sortField, sortOrder);
        List<Employee> employees = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalRows", page.getTotalElements());
        model.addAttribute("employees", employees);
        model.addAttribute("search", new SearchQuery());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("reverseSortOrder", sortOrder.equals("asc") ? "desc" : "asc");
        model.addAttribute("username", employeeService.getLoggedInUserUsername());

        return "index";
    }

    @GetMapping("/add")
    public String showEmployeePage(Model model) {
        model.addAttribute("heading", "Add New Employee");
        model.addAttribute("employee", new EmployeeDTO());
        model.addAttribute("username", employeeService.getLoggedInUserUsername());
        return "employee";
    }

    @GetMapping("/search")
    public String search(Model model, @ModelAttribute(name = "search") SearchQuery searchQuery) {
        if (searchQuery.getFirstName().equals("") && searchQuery.getLastName().equals("") && searchQuery.getEmail().equals(""))
            return "redirect:/";
        return search(model, 1, searchQuery.getFirstName(), searchQuery.getLastName(), searchQuery.getEmail(), "firstName", "asc");
    }

    @GetMapping("/search/{pageNo}")
    public String search(Model model,
                         @PathVariable("pageNo") Integer pageNo,
                         @RequestParam(name = "firstName") String firstName,
                         @RequestParam(name = "lastName") String lastName,
                         @RequestParam(name = "email") String email,
                         @RequestParam(name = "sortField") String sortField,
                         @RequestParam(name = "sortOrder") String sortOrder) {

        SearchQuery searchObject = new SearchQuery();
        searchObject.setFirstName(firstName);
        searchObject.setLastName(lastName);
        searchObject.setEmail(email);

        Page<Employee> page = employeeService.search(firstName, lastName, email, pageNo, PAGE_SIZE, sortField, sortOrder);
        List<Employee> employees = page.getContent();
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("email", email);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalRows", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("employees", employees);
        model.addAttribute("search", searchObject);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("reverseSortOrder", sortOrder.equals("asc") ? "desc" : "asc");
        model.addAttribute("username", employeeService.getLoggedInUserUsername());

        return "search";
    }

    @PostMapping("/save")
    public String saveEmployee(Model model, @ModelAttribute("employee") EmployeeDTO employeeDTO, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");
        if (referer.contains("add")) {
            if (employeeService.existsByEmail(employeeDTO.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email ID: " + employeeDTO.getEmail() + " already exists!");
                return "redirect:" + referer;
            } else if (employeeDTO.getPassword() == null || employeeDTO.getPassword().equals("")) {
                redirectAttributes.addFlashAttribute("error", "Please enter password!");
                return "redirect:" + referer;
            } else {
                Employee employee = new Employee();
                employee.setFirstName(employeeDTO.getFirstName());
                employee.setLastName(employeeDTO.getLastName());
                employee.setEmail(employeeDTO.getEmail());
                employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
                employee.setSalary(employeeDTO.getSalary());
                employee.setRoles(new ArrayList<Role>());
//                employeeService.saveEmployee(employee);
                Role role = roleService.findByName(employeeDTO.getType()).get();
                List<Role> roles = new ArrayList<>();
                roles.add(role);
                employee.setRoles(roles);
                employeeService.saveEmployee(employee);
                redirectAttributes.addFlashAttribute("success", "Employee added successfully");
                return "redirect:/";
            }
        }
        // else for update
        Employee employee = employeeService.getEmployee(employeeDTO.getId());
        // check if updated email already is assigned to someone else
        if (!employeeDTO.getEmail().equals(employee.getEmail()) && employeeService.existsByEmail(employeeDTO.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email ID: " + employeeDTO.getEmail() + " already exists!");
            return "redirect:" + referer;
        }
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        if (employeeDTO.getPassword() == null || employeeDTO.getPassword().length() == 0)
            employee.setPassword(employeeService.getEmployee(employeeDTO.getId()).getPassword());
        else
            employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        employee.setSalary(employeeDTO.getSalary());
        Role role = roleService.findByName(employeeDTO.getType()).get();
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        employee.setRoles(roles);
        employeeService.saveEmployee(employee);
        redirectAttributes.addFlashAttribute("success", "Employee with email ID: " + employee.getEmail() + " updated successfully");
        return "redirect:" + referer;
    }

    @GetMapping("/update/{id}")
    public String showUpdateEmployeePage(@PathVariable UUID id, Model model, HttpServletRequest request) {
        Employee employee = employeeService.getEmployee(id);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setPassword(null);
        employeeDTO.setSalary(employee.getSalary());
        final Role ROLE_ADMIN = roleService.findByName("ADMIN").get();
        if (employee.getRoles().contains(ROLE_ADMIN))
            employeeDTO.setType("ADMIN");
        else
            employeeDTO.setType("USER");
        model.addAttribute("heading", "Update Employee");
        model.addAttribute("passwordHelp", "dummy_text");
        model.addAttribute("employee", employeeDTO);
        model.addAttribute("username", employeeService.getLoggedInUserUsername());
        return "employee";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        Employee employee = employeeService.getEmployee(id);
        employee.setRoles(new ArrayList<>());
        employeeService.saveEmployee(employee);
        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute("error", "Employee deleted with email ID: " + employee.getEmail());
        return "redirect:/";
    }

}