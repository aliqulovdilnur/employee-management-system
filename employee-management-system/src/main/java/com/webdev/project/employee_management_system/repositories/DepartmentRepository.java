package com.webdev.project.employee_management_system.repositories;

import com.webdev.project.employee_management_system.entites.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByDepartmentName(String departmentName);
}
