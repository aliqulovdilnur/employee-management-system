package com.webdev.project.employee_management_system.controllers;

import com.webdev.project.employee_management_system.entites.Position;
import com.webdev.project.employee_management_system.services.PositionService;
import com.webdev.project.employee_management_system.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
public class PositionController {

    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    // GET all positions
    @GetMapping
    public ResponseEntity<?> getAllPositions() {
        try {
            List<Position> positions = positionService.getAllPositions();
            return ResponseUtil.success("Positions retrieved successfully", positions);
        } catch (Exception e) {
            return ResponseUtil.error("Failed to retrieve positions: " + e.getMessage());
        }
    }

    // GET position by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPositionById(@PathVariable Integer id) {
        try {
            return positionService.getPositionById(id)
                    .map(position -> ResponseUtil.success("Position retrieved successfully", position))
                    .orElse(ResponseUtil.error("Position not found with ID: " + id));
        } catch (Exception e) {
            return ResponseUtil.error("Error retrieving position: " + e.getMessage());
        }
    }

    // CREATE new position
    @PostMapping
    public ResponseEntity<?> createPosition(@RequestBody Position position) {
        try {
            Position savedPosition = positionService.createPosition(position);
            return ResponseUtil.success("Position created successfully", savedPosition);
        } catch (Exception e) {
            return ResponseUtil.error("Failed to create position: " + e.getMessage());
        }
    }

    // UPDATE existing position
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePosition(@PathVariable Integer id, @RequestBody Position position) {
        try {
            Position updatedPosition = positionService.updatePosition(id, position);
            return ResponseUtil.success("Position updated successfully", updatedPosition);
        } catch (RuntimeException e) {
            return ResponseUtil.error(e.getMessage());
        } catch (Exception e) {
            return ResponseUtil.error("Failed to update position: " + e.getMessage());
        }
    }

    // DELETE position by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePosition(@PathVariable Integer id) {
        try {
            positionService.deletePosition(id);
            return ResponseUtil.success("Position deleted successfully", null);
        } catch (RuntimeException e) {
            return ResponseUtil.error(e.getMessage());
        } catch (Exception e) {
            return ResponseUtil.error("Failed to delete position: " + e.getMessage());
        }
    }
}
