package com.webdev.project.employee_management_system.controllers;

import com.webdev.project.employee_management_system.entites.Employee;
import com.webdev.project.employee_management_system.responses.ErrorResponse;
import com.webdev.project.employee_management_system.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // Get all employees (ADMIN or HR_MANAGER)
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','HR_MANAGER')")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // Get employee by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','HR_MANAGER')")
    public ResponseEntity<?> getEmployeeById(@PathVariable Integer id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("EMP_001", "Employee not found with ID: " + id));
        }
    }

    // Create new employee
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','HR_MANAGER')")
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        try {
            // Allow null department or position for testing
            if (employee.getDepartment() == null) employee.setDepartment(null);
            if (employee.getPosition() == null) employee.setPosition(null);

            Employee created = employeeService.createEmployee(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("EMP_002", "Failed to create employee: " + e.getMessage()));
        }
    }

    // Update employee
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','HR_MANAGER')")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer id, @RequestBody Employee employee) {
        try {
            Employee updated = employeeService.updateEmployee(id, employee);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("EMP_003", "Employee not found with ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("EMP_004", "Failed to update employee: " + e.getMessage()));
        }
    }

    // Delete employee
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','HR_MANAGER')")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer id) {
        try {
            boolean deleted = employeeService.deleteEmployee(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("EMP_005", "Employee not found with ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("EMP_006", "Failed to delete employee: " + e.getMessage()));
        }
    }
}
