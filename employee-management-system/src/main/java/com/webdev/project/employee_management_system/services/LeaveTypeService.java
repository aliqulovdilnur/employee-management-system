package com.webdev.project.employee_management_system.services;

import com.webdev.project.employee_management_system.entites.LeaveType;
import com.webdev.project.employee_management_system.repositories.LeaveTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveTypeService(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
    }

    public LeaveType createLeaveType(LeaveType leaveType) {
        return leaveTypeRepository.save(leaveType);
    }

    public LeaveType updateLeaveType(Integer id, LeaveType updatedLeaveType) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("LeaveType not found with ID: " + id));

        leaveType.setLeaveTypeName(updatedLeaveType.getLeaveTypeName());
        return leaveTypeRepository.save(leaveType);
    }

    public void deleteLeaveType(Integer id) {
        if (!leaveTypeRepository.existsById(id)) {
            throw new NoSuchElementException("LeaveType not found with ID: " + id);
        }
        leaveTypeRepository.deleteById(id);
    }

    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }

    public LeaveType getLeaveTypeById(Integer id) {
        return leaveTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("LeaveType not found with ID: " + id));
    }
}
