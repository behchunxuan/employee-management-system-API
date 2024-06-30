package com.example.employee_management_system_API.controller;

import com.example.employee_management_system_API.dto.EmployeeDto;
import com.example.employee_management_system_API.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/create")
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        EmployeeDto employee = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, employeeDto);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/position/{position}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByPosition(@PathVariable String position) {
        List<EmployeeDto> employees = employeeService.getEmployeesByPosition(position);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByDepartment(@PathVariable Long departmentId) {
        List<EmployeeDto> employees = employeeService.getEmployeesByDepartment(departmentId);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PutMapping("/{employeeId}/assign-project/{projectId}")
    public ResponseEntity<EmployeeDto> assignProjectToEmployee(@PathVariable Long employeeId, @PathVariable Long projectId) {
        EmployeeDto updatedEmployee = employeeService.assignProjectToEmployee(employeeId, projectId);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}/remove-project/{projectId}")
    public ResponseEntity<EmployeeDto> removeProjectFromEmployee(@PathVariable Long employeeId, @PathVariable Long projectId) {
        EmployeeDto updatedEmployee = employeeService.removeProjectFromEmployee(projectId, employeeId);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @PutMapping("/{employeeId}/assign-department/{departmentId}")
    public ResponseEntity<EmployeeDto> assignDepartmentToEmployee(@PathVariable Long employeeId, @PathVariable Long departmentId) {
        EmployeeDto updatedEmployee = employeeService.assignDepartmentToEmployee(employeeId, departmentId);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}/remove-department")
    public ResponseEntity<EmployeeDto> removeEmployeeFromDepartment(@PathVariable Long employeeId) {
        EmployeeDto updatedEmployee = employeeService.removeEmployeeFromDepartment(employeeId);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }
}
