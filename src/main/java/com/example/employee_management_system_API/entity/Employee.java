package com.example.employee_management_system_API.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;

//@Data
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String position;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonManagedReference
    private Department department;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "employee_project",
            joinColumns = { @JoinColumn(name = "employee_id") },
            inverseJoinColumns = { @JoinColumn(name = "project_id") })
    @JsonManagedReference
    private Set<Project> projects = new HashSet<>();

    public Employee() {
    }

    public Employee(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Employee(Long id, String name, Department department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Project> getProjects() {
        if (projects == null) {
            projects = new HashSet<>();
        }
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public void addProject(Project project) {
        if (this.projects == null) {
            this.projects = new HashSet<>();
        }
        this.projects.add(project);
        project.getEmployees().add(this);
    }

    public void removeProject(long projectId) {
        Set<Project> updatedProjects = new HashSet<>(this.projects);
        for (Project project : updatedProjects) {
            if (project.getId().equals(projectId)) {
                this.projects.remove(project);
                project.getEmployees().remove(this);
            }
        }
    }


}
