package com.webdev.project.employee_management_system.service_test;

import com.webdev.project.employee_management_system.entites.Department;
import com.webdev.project.employee_management_system.repositories.DepartmentRepository;
import com.webdev.project.employee_management_system.services.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department dept1;
    private Department dept2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dept1 = new Department();
        dept1.setDepartmentName("HR");
        dept1.setLocation("Building A");

        dept2 = new Department();
        dept2.setDepartmentName("IT");
        dept2.setLocation("Building B");
    }

    @Test
    @DisplayName("Should return all departments from repository")
    void testGetAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(dept1, dept2));

        List<Department> result = departmentService.getAllDepartments();

        assertEquals(2, result.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return department by ID when it exists")
    void testGetDepartmentById_Found() {
        when(departmentRepository.findById(1)).thenReturn(Optional.of(dept1));

        Optional<Department> result = departmentService.getDepartmentById(1);

        assertTrue(result.isPresent());
        assertEquals("HR", result.get().getDepartmentName());
        verify(departmentRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should create a new department successfully")
    void testCreateDepartment() {
        when(departmentRepository.save(dept1)).thenReturn(dept1);

        Department result = departmentService.createDepartment(dept1);

        assertNotNull(result);
        assertEquals("HR", result.getDepartmentName());
        verify(departmentRepository, times(1)).save(dept1);
    }

    @Test
    @DisplayName("Should update existing department with new details")
    void testUpdateDepartment_Found() {
        Department updated = new Department();
        updated.setDepartmentName("Finance");
        updated.setLocation("Building C");

        when(departmentRepository.findById(1)).thenReturn(Optional.of(dept1));
        when(departmentRepository.save(any(Department.class))).thenReturn(updated);

        Department result = departmentService.updateDepartment(1, updated);

        assertEquals("Finance", result.getDepartmentName());
        assertEquals("Building C", result.getLocation());
        verify(departmentRepository, times(1)).findById(1);
        verify(departmentRepository, times(1)).save(dept1);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent department")
    void testUpdateDepartment_NotFound() {
        when(departmentRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            departmentService.updateDepartment(99, dept1);
        });

        assertEquals("Department not found with ID: 99", exception.getMessage());
        verify(departmentRepository, times(1)).findById(99);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent department")
    void testDeleteDepartment_NotFound() {
        when(departmentRepository.existsById(99)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            departmentService.deleteDepartment(99);
        });

        assertEquals("Department not found with ID: 99", exception.getMessage());
        verify(departmentRepository, times(1)).existsById(99);
    }
}
