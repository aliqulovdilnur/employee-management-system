package com.webdev.project.employee_management_system.repositories;

import com.webdev.project.employee_management_system.entites.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
}
