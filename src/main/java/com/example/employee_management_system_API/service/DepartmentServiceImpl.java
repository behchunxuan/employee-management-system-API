package com.example.employee_management_system_API.service;

import com.example.employee_management_system_API.dto.DepartmentDto;
import com.example.employee_management_system_API.dto.EmployeeDto;
import com.example.employee_management_system_API.entity.Department;
import com.example.employee_management_system_API.entity.Employee;
import com.example.employee_management_system_API.exceptions.DepartmentNotFoundException;
import com.example.employee_management_system_API.exceptions.EmployeeNotFoundException;
import com.example.employee_management_system_API.repository.DepartmentRepository;
import com.example.employee_management_system_API.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = {"departments"})
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setName(departmentDto.getName());
        Department savedDepartment = departmentRepository.save(department);
        return mapToDto(savedDepartment);
    }

    @Override
    @Transactional
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Cacheable(key = "#id")
    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
        return mapToDto(department);
    }

    @Override
    @Transactional
    @CachePut(key = "#id")
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDetails) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
        department.setName(departmentDetails.getName());
        Department updatedDepartment = departmentRepository.save(department);
        return mapToDto(updatedDepartment);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    private DepartmentDto mapToDto(Department department) {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(department.getId());
        departmentDto.setName(department.getName());
        return departmentDto;
    }

}
