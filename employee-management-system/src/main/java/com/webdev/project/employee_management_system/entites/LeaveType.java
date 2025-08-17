package com.webdev.project.employee_management_system.entites;

import jakarta.persistence.*;

@Entity
@Table(name = "leave_type")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer leaveTypeId;

    @Column(name = "leave_type_name", nullable = false, unique = true)
    private String leaveTypeName;

    @Column
    private String description;

    // Constructors
    public LeaveType() {}

    public LeaveType(String leaveTypeName, String description) {
        this.leaveTypeName = leaveTypeName;
        this.description = description;
    }

    public LeaveType(Integer leaveTypeId, String leaveTypeName, String description) {
        this.leaveTypeId = leaveTypeId;
        this.leaveTypeName = leaveTypeName;
        this.description = description;
    }

    // Getters and Setters
    public Integer getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(Integer leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
