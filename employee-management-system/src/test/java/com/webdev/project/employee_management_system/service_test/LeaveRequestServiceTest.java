package com.webdev.project.employee_management_system.service_test;

import com.webdev.project.employee_management_system.entites.Employee;
import com.webdev.project.employee_management_system.entites.LeaveRequest;
import com.webdev.project.employee_management_system.entites.LeaveType;
import com.webdev.project.employee_management_system.repositories.EmployeeRepository;
import com.webdev.project.employee_management_system.repositories.LeaveRequestRepository;
import com.webdev.project.employee_management_system.repositories.LeaveTypeRepository;
import com.webdev.project.employee_management_system.services.LeaveRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeaveRequestServiceTest {

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LeaveTypeRepository leaveTypeRepository;

    private Employee employee;
    private LeaveType leaveType;
    private LeaveRequest leaveRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1);

        leaveType = new LeaveType();
        leaveType.setLeaveTypeId(1);

        leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setStartTime(LocalDateTime.now());
        leaveRequest.setEndTime(LocalDateTime.now().plusDays(3));

    }

    @Test
    @DisplayName("createLeaveRequest sets status to PENDING and saves")
    void testCreateLeaveRequest_Success() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(leaveTypeRepository.findById(1)).thenReturn(Optional.of(leaveType));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(leaveRequest);

        LeaveRequest created = leaveRequestService.createLeaveRequest(leaveRequest);

        assertNotNull(created);
        assertEquals("PENDING", created.getStatus());
        verify(leaveRequestRepository).save(leaveRequest);
    }

    @Test
    @DisplayName("createLeaveRequest throws if employee not found")
    void testCreateLeaveRequest_EmployeeNotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> leaveRequestService.createLeaveRequest(leaveRequest));
        assertTrue(ex.getMessage().contains("Employee not found"));
    }

    @Test
    @DisplayName("updateLeaveRequestStatus changes status")
    void testUpdateLeaveRequestStatus() {
        leaveRequest.setStatus("PENDING");
        when(leaveRequestRepository.findById(1)).thenReturn(Optional.of(leaveRequest));
        when(leaveRequestRepository.save(leaveRequest)).thenReturn(leaveRequest);

        LeaveRequest updated = leaveRequestService.updateLeaveRequestStatus(1, "APPROVED");

        assertEquals("APPROVED", updated.getStatus());
        verify(leaveRequestRepository).save(leaveRequest);
    }

    @Test
    @DisplayName("deleteLeaveRequest throws if not found")
    void testDeleteLeaveRequest_NotFound() {
        when(leaveRequestRepository.existsById(1)).thenReturn(false);

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> leaveRequestService.deleteLeaveRequest(1));
        assertTrue(ex.getMessage().contains("LeaveRequest not found"));
    }
}
