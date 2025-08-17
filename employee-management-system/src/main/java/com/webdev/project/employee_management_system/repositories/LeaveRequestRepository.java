package com.webdev.project.employee_management_system.repositories;

import com.webdev.project.employee_management_system.entites.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {}
