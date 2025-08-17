package com.webdev.project.employee_management_system.services;

import com.webdev.project.employee_management_system.entites.Position;
import com.webdev.project.employee_management_system.repositories.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public Optional<Position> getPositionById(Integer id) {
        return positionRepository.findById(id);
    }

    public Position createPosition(Position position) {
        return positionRepository.save(position);
    }

    public Position updatePosition(Integer id, Position updatedPosition) {
        return positionRepository.findById(id)
                .map(position -> {
                    position.setPositionTitle(updatedPosition.getPositionTitle());
                    position.setDepartment(updatedPosition.getDepartment());
                    position.setSalaryGrade(updatedPosition.getSalaryGrade());
                    position.setJobDescription(updatedPosition.getJobDescription());
                    return positionRepository.save(position);
                })
                .orElseThrow(() -> new RuntimeException("Position not found with ID: " + id));
    }

    public void deletePosition(Integer id) {
        positionRepository.deleteById(id);
    }
}
