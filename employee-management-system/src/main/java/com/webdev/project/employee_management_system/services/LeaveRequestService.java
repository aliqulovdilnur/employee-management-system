package com.webdev.project.employee_management_system.services;

import com.webdev.project.employee_management_system.entites.Employee;
import com.webdev.project.employee_management_system.entites.LeaveRequest;
import com.webdev.project.employee_management_system.entites.LeaveType;
import com.webdev.project.employee_management_system.repositories.EmployeeRepository;
import com.webdev.project.employee_management_system.repositories.LeaveRequestRepository;
import com.webdev.project.employee_management_system.repositories.LeaveTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
                               EmployeeRepository employeeRepository,
                               LeaveTypeRepository leaveTypeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
    }

    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        // Validate Employee
        Employee employee = employeeRepository.findById(leaveRequest.getEmployee().getId())
                .orElseThrow(() -> new NoSuchElementException("Employee not found with ID: " + leaveRequest.getEmployee().getId()));
        leaveRequest.setEmployee(employee);

        // Validate LeaveType
        LeaveType leaveType = leaveTypeRepository.findById(leaveRequest.getLeaveType().getLeaveTypeId())
                .orElseThrow(() -> new NoSuchElementException("LeaveType not found with ID: " + leaveRequest.getLeaveType().getLeaveTypeId()));
        leaveRequest.setLeaveType(leaveType);

        // Default status
        leaveRequest.setStatus("PENDING");

        return leaveRequestRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest getLeaveRequestById(Integer id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("LeaveRequest not found with ID: " + id));
    }

    public LeaveRequest updateLeaveRequestStatus(Integer id, String status) {
        LeaveRequest leaveRequest = getLeaveRequestById(id);
        leaveRequest.setStatus(status);
        return leaveRequestRepository.save(leaveRequest);
    }

    public void deleteLeaveRequest(Integer id) {
        if (!leaveRequestRepository.existsById(id)) {
            throw new NoSuchElementException("LeaveRequest not found with ID: " + id);
        }
        leaveRequestRepository.deleteById(id);
    }
}
