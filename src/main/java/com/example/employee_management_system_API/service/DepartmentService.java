package com.example.employee_management_system_API.service;

import com.example.employee_management_system_API.dto.DepartmentDto;
import com.example.employee_management_system_API.dto.EmployeeDto;

import java.util.List;

public interface DepartmentService {
    DepartmentDto createDepartment(DepartmentDto departmentDto);
    List<DepartmentDto> getAllDepartments();
    DepartmentDto getDepartmentById(Long id);
    DepartmentDto updateDepartment(Long id, DepartmentDto departmentDetails);
    void deleteDepartment(Long id);
}
