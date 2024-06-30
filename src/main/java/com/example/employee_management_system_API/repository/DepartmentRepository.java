package com.example.employee_management_system_API.repository;

import com.example.employee_management_system_API.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}