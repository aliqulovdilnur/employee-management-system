package com.webdev.project.employee_management_system.services;

import com.webdev.project.employee_management_system.entites.Department;
import com.webdev.project.employee_management_system.entites.Employee;
import com.webdev.project.employee_management_system.entites.Position;
import com.webdev.project.employee_management_system.entites.User;
import com.webdev.project.employee_management_system.repositories.DepartmentRepository;
import com.webdev.project.employee_management_system.repositories.EmployeeRepository;
import com.webdev.project.employee_management_system.repositories.PositionRepository;
import com.webdev.project.employee_management_system.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final UserRepository userRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           PositionRepository positionRepository,
                           UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.userRepository = userRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with ID: " + id));
    }

    @Transactional
    public Employee createEmployee(Employee employee) {
        // Validate Department
        if (employee.getDepartment() != null && employee.getDepartment().getDepartmentId() != null) {
            Department department = departmentRepository.findById(employee.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new NoSuchElementException("Department not found with ID: " + employee.getDepartment().getDepartmentId()));
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }

        // Validate Position
        if (employee.getPosition() != null && employee.getPosition().getPositionId() != null) {
            Position position = positionRepository.findById(employee.getPosition().getPositionId())
                    .orElseThrow(() -> new NoSuchElementException("Position not found with ID: " + employee.getPosition().getPositionId()));
            employee.setPosition(position);
        } else {
            employee.setPosition(null);
        }

        // Validate User
        if (employee.getUser() != null && employee.getUser().getId() != null) {
            User user = userRepository.findById(employee.getUser().getId())
                    .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + employee.getUser().getId()));
            employee.setUser(user);
        } else {
            employee.setUser(null);
        }

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Integer id, Employee updatedEmployee) {
        Employee employee = getEmployeeById(id);

        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setPhoneNumber(updatedEmployee.getPhoneNumber());
        employee.setHireDate(updatedEmployee.getHireDate());

        // Department
        if (updatedEmployee.getDepartment() != null && updatedEmployee.getDepartment().getDepartmentId() != null) {
            Department department = departmentRepository.findById(updatedEmployee.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new NoSuchElementException("Department not found with ID: " + updatedEmployee.getDepartment().getDepartmentId()));
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }

        // Position
        if (updatedEmployee.getPosition() != null && updatedEmployee.getPosition().getPositionId() != null) {
            Position position = positionRepository.findById(updatedEmployee.getPosition().getPositionId())
                    .orElseThrow(() -> new NoSuchElementException("Position not found with ID: " + updatedEmployee.getPosition().getPositionId()));
            employee.setPosition(position);
        } else {
            employee.setPosition(null);
        }

        // User
        if (updatedEmployee.getUser() != null && updatedEmployee.getUser().getId() != null) {
            User user = userRepository.findById(updatedEmployee.getUser().getId())
                    .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + updatedEmployee.getUser().getId()));
            employee.setUser(user);
        } else {
            employee.setUser(null);
        }

        return employeeRepository.save(employee);
    }

    @Transactional
    public boolean deleteEmployee(Integer id) {
        if (!employeeRepository.existsById(id)) {
            return false;
        }
        employeeRepository.deleteById(id);
        return true;
    }
}
