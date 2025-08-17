package com.webdev.project.employee_management_system.service_test;

import com.webdev.project.employee_management_system.entites.Department;
import com.webdev.project.employee_management_system.entites.Employee;
import com.webdev.project.employee_management_system.repositories.DepartmentRepository;
import com.webdev.project.employee_management_system.repositories.EmployeeRepository;
import com.webdev.project.employee_management_system.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    private Employee employee;
    private Department department;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        department = new Department("IT", "Tashkent");
        department.setDepartmentId(1);

        employee = new Employee("Alice", "Smith", "alice@example.com",
                "998901234567", LocalDate.now(), department, null, null);
        employee.setId(1);
    }

    @Test
    @DisplayName("getEmployeeById should return employee if exists")
    void testGetEmployeeById_Found() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        Employee found = employeeService.getEmployeeById(1);
        assertEquals("Alice", found.getFirstName());
        verify(employeeRepository).findById(1);
    }

    @Test
    @DisplayName("getEmployeeById should throw exception if not found")
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> employeeService.getEmployeeById(2));
    }

    @Test
    @DisplayName("createEmployee should save employee with valid department")
    void testCreateEmployee_WithDepartment() {
        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee created = employeeService.createEmployee(employee);

        assertNotNull(created);
        assertEquals("Alice", created.getFirstName());
        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("deleteEmployee should return true if employee exists")
    void testDeleteEmployee_Exists() {
        when(employeeRepository.existsById(1)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1);

        assertTrue(employeeService.deleteEmployee(1));
        verify(employeeRepository).deleteById(1);
    }

    @Test
    @DisplayName("deleteEmployee should return false if employee does not exist")
    void testDeleteEmployee_NotExists() {
        when(employeeRepository.existsById(2)).thenReturn(false);

        assertFalse(employeeService.deleteEmployee(2));
        verify(employeeRepository, never()).deleteById(2);
    }
}
