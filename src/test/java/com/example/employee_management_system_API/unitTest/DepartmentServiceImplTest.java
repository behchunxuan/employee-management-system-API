package com.example.employee_management_system_API.unitTest;

import com.example.employee_management_system_API.dto.DepartmentDto;
import com.example.employee_management_system_API.entity.Department;
import com.example.employee_management_system_API.exceptions.DepartmentNotFoundException;
import com.example.employee_management_system_API.repository.DepartmentRepository;
import com.example.employee_management_system_API.service.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    private DepartmentDto departmentDto;

    @BeforeEach
    public void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("HR");

        departmentDto = new DepartmentDto();
        departmentDto.setId(1L);
        departmentDto.setName("HR");
    }

    @DisplayName("Creat a department")
    @Test
    public void testCreateDepartment() {
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentDto result = departmentService.createDepartment(departmentDto);

        assertNotNull(result);
        assertEquals("HR", result.getName());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @DisplayName("Retrieve all departments")
    @Test
    public void testGetAllDepartments() {
        List<Department> departments = Arrays.asList(
                new Department(1L, "HR"),
                new Department(2L, "IT"),
                new Department(3L, "QA")
        );
        when(departmentRepository.findAll()).thenReturn(departments);

        List<DepartmentDto> result = departmentService.getAllDepartments();

        assertEquals(3, result.size());
        assertEquals("HR", result.get(0).getName());
        assertEquals("IT", result.get(1).getName());
        assertEquals("QA", result.get(2).getName());
    }

    @DisplayName("Retrieve a department by ID")
    @Test
    public void testGetDepartmentById() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        DepartmentDto result = departmentService.getDepartmentById(1L);

        assertNotNull(result);
        assertEquals("HR", result.getName());
    }

    @DisplayName("Retrieve a department by ID - Not Found")
    @Test
    public void testGetDepartmentById_NotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getDepartmentById(1L));
    }

    @DisplayName("Update a department")
    @Test
    public void testUpdateDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentDto updatedDto = new DepartmentDto();
        updatedDto.setName("IT");

        DepartmentDto result = departmentService.updateDepartment(1L, updatedDto);

        assertNotNull(result);
        assertEquals("IT", result.getName());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @DisplayName("Delete a department")
    @Test
    public void testDeleteDepartment() {
        doNothing().when(departmentRepository).deleteById(1L);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository, times(1)).deleteById(1L);
    }
}
