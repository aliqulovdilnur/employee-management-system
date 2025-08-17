package com.webdev.project.employee_management_system.service_test;

import com.webdev.project.employee_management_system.entites.Position;
import com.webdev.project.employee_management_system.repositories.PositionRepository;
import com.webdev.project.employee_management_system.services.PositionService;
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

class PositionServiceTest {

    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private PositionService positionService;

    private Position position1;
    private Position position2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        position1 = new Position();
        position1.setPositionTitle("Developer");
        position1.setSalaryGrade(5);

        position2 = new Position();
        position2.setPositionTitle("Manager");
        position2.setSalaryGrade(8);
    }

    @Test
    @DisplayName("Should return all positions from repository")
    void testGetAllPositions() {
        when(positionRepository.findAll()).thenReturn(Arrays.asList(position1, position2));

        List<Position> result = positionService.getAllPositions();

        assertEquals(2, result.size());
        verify(positionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return position by ID when it exists")
    void testGetPositionById_Found() {
        when(positionRepository.findById(1)).thenReturn(Optional.of(position1));

        Optional<Position> result = positionService.getPositionById(1);

        assertTrue(result.isPresent());
        assertEquals("Developer", result.get().getPositionTitle());
        verify(positionRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should create a new position successfully")
    void testCreatePosition() {
        when(positionRepository.save(position1)).thenReturn(position1);

        Position result = positionService.createPosition(position1);

        assertNotNull(result);
        assertEquals("Developer", result.getPositionTitle());
        verify(positionRepository, times(1)).save(position1);
    }

    @Test
    @DisplayName("Should update existing position with new details")
    void testUpdatePosition_Found() {
        Position updated = new Position();
        updated.setPositionTitle("Senior Developer");
        updated.setSalaryGrade(6);

        when(positionRepository.findById(1)).thenReturn(Optional.of(position1));
        when(positionRepository.save(any(Position.class))).thenReturn(updated);

        Position result = positionService.updatePosition(1, updated);

        assertEquals("Senior Developer", result.getPositionTitle());
        assertEquals(6, result.getSalaryGrade());
        verify(positionRepository, times(1)).findById(1);
        verify(positionRepository, times(1)).save(position1);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent position")
    void testUpdatePosition_NotFound() {
        when(positionRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            positionService.updatePosition(99, position1);
        });

        assertEquals("Position not found with ID: 99", exception.getMessage());
        verify(positionRepository, times(1)).findById(99);
    }
}
