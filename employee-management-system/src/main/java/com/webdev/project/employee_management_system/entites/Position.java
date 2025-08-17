package com.webdev.project.employee_management_system.entites;

import jakarta.persistence.*;

@Entity
@Table(name = "position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer positionId;

    @Column(name = "position_title", nullable = false)
    private String positionTitle;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "salary_grade")
    private Integer salaryGrade;

    @Column(name = "job_description")
    private String jobDescription;

    // Constructors
    public Position() {}

    public Position(String positionTitle, Department department, Integer salaryGrade, String jobDescription) {
        this.positionTitle = positionTitle;
        this.department = department;
        this.salaryGrade = salaryGrade;
        this.jobDescription = jobDescription;
    }

    // Getters & Setters
    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getSalaryGrade() {
        return salaryGrade;
    }

    public void setSalaryGrade(Integer salaryGrade) {
        this.salaryGrade = salaryGrade;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}
