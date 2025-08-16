package com.webdev.project.employee_management_system.entites;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;

    @Column(name = "department_name", nullable = false, unique = true)
    private String departmentName;

    @Column(name = "location")
    private String location;

    // Constructors
    public Department() {}

    public Department(String departmentName, String location) {
        this.departmentName = departmentName;
        this.location = location;
    }

    // Getters & Setters
    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
