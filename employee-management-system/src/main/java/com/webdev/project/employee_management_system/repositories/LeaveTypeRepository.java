package com.webdev.project.employee_management_system.repositories;

import com.webdev.project.employee_management_system.entites.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Integer> {

}
