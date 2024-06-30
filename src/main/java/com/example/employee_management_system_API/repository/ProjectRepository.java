package com.example.employee_management_system_API.repository;

import com.example.employee_management_system_API.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}