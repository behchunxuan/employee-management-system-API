package com.example.employee_management_system_API.service;

import com.example.employee_management_system_API.dto.EmployeeDto;
import com.example.employee_management_system_API.entity.Department;
import com.example.employee_management_system_API.entity.Employee;
import com.example.employee_management_system_API.entity.Project;
import com.example.employee_management_system_API.exceptions.DepartmentNotFoundException;
import com.example.employee_management_system_API.exceptions.EmployeeNotFoundException;
import com.example.employee_management_system_API.exceptions.ProjectNotFoundException;
import com.example.employee_management_system_API.repository.DepartmentRepository;
import com.example.employee_management_system_API.repository.EmployeeRepository;
import com.example.employee_management_system_API.repository.ProjectRepository;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames={"employees"})
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, ProjectRepository projectRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        // Create a new Employee entity from the DTO
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setPosition(employeeDto.getPosition());

        // Set the department
        if (employeeDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + employeeDto.getDepartmentId()));
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }

        // Set the projects
        if (employeeDto.getProjectIds() != null && !employeeDto.getProjectIds().isEmpty()) {
            employee.setProjects(employeeDto.getProjectIds().stream()
                    .map(projectId -> projectRepository.findById(projectId)
                            .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId)))
                    .collect(Collectors.toSet()));
        }

        // Save the employee
        Employee newEmployee = employeeRepository.save(employee);
        return mapToDto(newEmployee);
    }

    @Override
    @Transactional
    public List<EmployeeDto> getAllEmployees() {
        // Retrieve all employees
        return employeeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Cacheable(key = "#id")
    public EmployeeDto getEmployeeById(Long id) {
        // Find the employee by ID
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        Hibernate.initialize(employee.getProjects());

        System.out.println("Get Employee " + id);
        return mapToDto(employee);
    }

    @Override
    @Transactional
    @CachePut(key = "#id")
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        // Find the employee by ID
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        // Update the employee details
        employee.setName(employeeDto.getName());
        employee.setPosition(employeeDto.getPosition());

        // Set the department
        employee.setDepartment(departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + employeeDto.getDepartmentId())));

        // Set the projects
        if (employeeDto.getProjectIds() != null && !employeeDto.getProjectIds().isEmpty()) {
            employee.setProjects(employeeDto.getProjectIds().stream()
                    .map(projectId -> projectRepository.findById(projectId)
                            .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId)))
                    .collect(Collectors.toSet()));
        }

        // Save the updated employee
        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToDto(updatedEmployee);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteEmployee(Long id) {
        // Delete the employee by ID
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional
    @Cacheable
    public List<EmployeeDto> getEmployeesByPosition(String position) {
        // Find employees by position
        List<Employee> employees = employeeRepository.findByPosition(position);
        if (employees.isEmpty()) {
            throw new EmployeeNotFoundException("No employees found with position: " + position);
        }

        return employees.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Cacheable
    public List<EmployeeDto> getEmployeesByDepartment(Long departmentId) {
        // Find employees by department ID
        List<Employee> employees = employeeRepository.findByDepartmentId(departmentId);
        if (employees.isEmpty()) {
            throw new DepartmentNotFoundException("Employees not found for department with id: " + departmentId);
        }

        return employees.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CachePut(key = "#employeeId")
    public EmployeeDto assignProjectToEmployee(Long employeeId, Long projectId) {
        // Find the employee by ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        // Find the project by ID
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        employee.setProjects(new HashSet<>(employee.getProjects()));

        employee.addProject(project);

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToDto(updatedEmployee);
    }

    @Override
    @Transactional
    @CachePut(key = "#employeeId")
    public EmployeeDto removeProjectFromEmployee(Long projectId, Long employeeId) {
        // Find employee by ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
        // Find project by ID
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        if (!employee.getProjects().contains(project)) {
            throw new ProjectNotFoundException("Project with id " + projectId + " is not assigned to employee with id " + employeeId);
        }

        employee.setProjects(new HashSet<>(employee.getProjects()));

        // Remove project from employee
        employee.removeProject(project.getId());
        Employee updatedEmployee = employeeRepository.save(employee);

        return mapToDto(updatedEmployee);
    }

    @Override
    @CachePut(key = "#employeeId")
    @Transactional
    public EmployeeDto assignDepartmentToEmployee(Long employeeId, Long departmentId) {
        // Find the employee by ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        // Find the department by ID
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + departmentId));

        employee.setDepartment(department);

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToDto(updatedEmployee);
    }

    @Override
    @CachePut(key = "#employeeId")
    @Transactional
    public EmployeeDto removeEmployeeFromDepartment(Long employeeId) {
        // Find the employee by ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        if (employee.getDepartment() == null) {
            throw new DepartmentNotFoundException("Employee with id " + employeeId + " is not associated with any department.");
        }

        // Remove the association with the department
        employee.setDepartment(null);

        // Save the updated employee
        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToDto(updatedEmployee);
    }

    // Map Employee entity to EmployeeDto
    private EmployeeDto mapToDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setName(employee.getName());
        employeeDto.setPosition(employee.getPosition());

        // Set department ID if available
        if (employee.getDepartment() != null) {
            employeeDto.setDepartmentId(employee.getDepartment().getId());
        }

        employeeDto.setProjectIds(employee.getProjects().stream()
                .map(Project::getId)
                .collect(Collectors.toSet()));

        return employeeDto;
    }
}
