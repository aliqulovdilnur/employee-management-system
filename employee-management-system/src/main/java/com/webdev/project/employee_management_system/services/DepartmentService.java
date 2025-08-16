package com.webdev.project.employee_management_system.services;

import com.webdev.project.employee_management_system.entites.Department;
import com.webdev.project.employee_management_system.repositories.DepartmentRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Integer id) {
        return departmentRepository.findById(id);
    }

    @Transactional
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Transactional
    public Department updateDepartment(Integer id, Department updatedDepartment) {
        return departmentRepository.findById(id).map(department -> {
            department.setDepartmentName(updatedDepartment.getDepartmentName());
            department.setLocation(updatedDepartment.getLocation());
            return departmentRepository.save(department);
        }).orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));
    }

    @Transactional
    public boolean deleteDepartment(Integer id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with ID: " + id);
        }
        departmentRepository.deleteById(id);
        return true;
    }

}
