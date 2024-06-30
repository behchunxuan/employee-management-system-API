package com.example.employee_management_system_API.unitTest;

import com.example.employee_management_system_API.dto.EmployeeDto;
import com.example.employee_management_system_API.entity.Department;
import com.example.employee_management_system_API.entity.Employee;
import com.example.employee_management_system_API.entity.Project;
import com.example.employee_management_system_API.repository.DepartmentRepository;
import com.example.employee_management_system_API.repository.EmployeeRepository;
import com.example.employee_management_system_API.repository.ProjectRepository;
import com.example.employee_management_system_API.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDto employeeDto;
    private Department department;
    private Project project;

    @BeforeEach
    public void setUp() {
        department = new Department(1L, "IT");
        project = new Project(1L, "Project A");

        employee = new Employee();
        employee.setId(1L);
        employee.setName("John");
        employee.setPosition("Developer");
        employee.setDepartment(department);
        employee.setProjects(Set.of(project));

        employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setName("John");
        employeeDto.setPosition("Developer");
        employeeDto.setDepartmentId(1L);
        employeeDto.setProjectIds(Set.of(1L));
    }

    @DisplayName("Create Employee")
    @Test
    public void testCreateEmployee() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto result = employeeService.createEmployee(employeeDto);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("Developer", result.getPosition());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @DisplayName("Get All Employees")
    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "John"),
                new Employee(2L, "YM"),
                new Employee(3L, "CX")
        );
        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeDto> result = employeeService.getAllEmployees();

        assertEquals(3, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("YM", result.get(1).getName());
        assertEquals("CX", result.get(2).getName());
    }

    @DisplayName("Get Employee By ID")
    @Test
    public void testGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDto result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("Developer", result.getPosition());
    }

    @DisplayName("Update Employee")
    @Test
    public void testUpdateEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto updatedDto = new EmployeeDto();
        updatedDto.setName("John Updated");
        updatedDto.setPosition("Senior Developer");
        updatedDto.setDepartmentId(1L);
        updatedDto.setProjectIds(Set.of(1L));

        EmployeeDto result = employeeService.updateEmployee(1L, updatedDto);

        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        assertEquals("Senior Developer", result.getPosition());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @DisplayName("Delete Employee")
    @Test
    public void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @DisplayName("Assign Project to Employee")
    @Test
    public void testAssignProjectToEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto result = employeeService.assignProjectToEmployee(1L, 1L);

        assertNotNull(result);
        assertTrue(result.getProjectIds().contains(1L));
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @DisplayName("Remove Project from Employee")
    @Test
    public void testRemoveProjectFromEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto result = employeeService.removeProjectFromEmployee(1L, 1L);

        assertNotNull(result);
        assertFalse(result.getProjectIds().contains(1L));
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @DisplayName("Assign Department to Employee")
    @Test
    public void testAssignDepartmentToEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto result = employeeService.assignDepartmentToEmployee(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getDepartmentId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @DisplayName("Remove Employee from Department")
    @Test
    public void testRemoveEmployeeFromDepartment() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto result = employeeService.removeEmployeeFromDepartment(1L);

        assertNotNull(result);
        assertNull(result.getDepartmentId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @DisplayName("Get Employees by Department")
    @Test
    public void testGetEmployeesByDepartment() {
        when(employeeRepository.findByDepartmentId(1L)).thenReturn(Collections.singletonList(employee));

        var result = employeeService.getEmployeesByDepartment(1L);

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals(1L, result.get(0).getDepartmentId());
    }

    @DisplayName("Get Employees by Position")
    @Test
    public void testGetEmployeesByPosition() {
        when(employeeRepository.findByPosition("Developer")).thenReturn(Collections.singletonList(employee));

        var result = employeeService.getEmployeesByPosition("Developer");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Developer", result.get(0).getPosition());
    }
}
