package com.example.employee_management_system_API.service;

import com.example.employee_management_system_API.dto.EmployeeDto;
import java.util.List;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    List<EmployeeDto> getAllEmployees();
    EmployeeDto getEmployeeById(Long id);
    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDetails);
    void deleteEmployee(Long id);
    List<EmployeeDto> getEmployeesByPosition(String position);
    List<EmployeeDto> getEmployeesByDepartment(Long departmentId);

    EmployeeDto assignProjectToEmployee(Long employeeId, Long projectId);
    EmployeeDto removeProjectFromEmployee(Long employeeId, Long projectId);

    EmployeeDto assignDepartmentToEmployee(Long employeeId, Long departmentId);
    EmployeeDto removeEmployeeFromDepartment(Long employeeId);
}
