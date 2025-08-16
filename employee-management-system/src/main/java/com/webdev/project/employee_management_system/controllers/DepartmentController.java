package com.webdev.project.employee_management_system.controllers;

import com.webdev.project.employee_management_system.entites.Department;
import com.webdev.project.employee_management_system.responses.ErrorResponse;
import com.webdev.project.employee_management_system.services.DepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // ---------------- Get All Departments ----------------
    @GetMapping
    public ResponseEntity<?> getAllDepartments() {
        try {
            List<Department> departments = departmentService.getAllDepartments();
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DEP_001", "Failed to fetch departments: " + e.getMessage()));
        }
    }

    // ---------------- Get Department by ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable Integer id) {
        try {
            Optional<Department> department = departmentService.getDepartmentById(id);
            if (department.isPresent()) {
                return ResponseEntity.ok(department.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("DEP_002", "Department not found with ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DEP_003", "Error fetching department: " + e.getMessage()));
        }
    }

    // ---------------- Create Department ----------------
    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody Department department) {
        try {
            Department createdDepartment = departmentService.createDepartment(department);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DEP_004", "Failed to create department: " + e.getMessage()));
        }
    }

    // ---------------- Update Department ----------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable Integer id, @RequestBody Department department) {
        try {
            Department updatedDepartment = departmentService.updateDepartment(id, department);
            return ResponseEntity.ok(updatedDepartment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("DEP_005", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DEP_006", "Failed to update department: " + e.getMessage()));
        }
    }

    // ---------------- Delete Department ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Integer id) {
        try {
                boolean deleted = departmentService.deleteDepartment(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("DEP_007", "Department not found with ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DEP_008", "Failed to delete department: " + e.getMessage()));
        }
    }
}
