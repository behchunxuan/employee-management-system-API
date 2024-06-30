package com.example.employee_management_system_API.repository;

import com.example.employee_management_system_API.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByPosition(String position);
    List<Employee> findByDepartmentId(Long departmentId);
}