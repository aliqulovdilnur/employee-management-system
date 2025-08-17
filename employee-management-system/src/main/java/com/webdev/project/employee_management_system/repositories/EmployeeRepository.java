package com.webdev.project.employee_management_system.repositories;

import com.webdev.project.employee_management_system.entites.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
