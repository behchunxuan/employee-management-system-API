package com.example.employee_management_system_API.integrationTest;

import com.example.employee_management_system_API.dto.EmployeeDto;
import com.example.employee_management_system_API.entity.Department;
import com.example.employee_management_system_API.entity.Employee;
import com.example.employee_management_system_API.entity.Project;
import com.example.employee_management_system_API.repository.DepartmentRepository;
import com.example.employee_management_system_API.repository.EmployeeRepository;
import com.example.employee_management_system_API.repository.ProjectRepository;
import com.example.employee_management_system_API.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Employee employee;
    private Project project;
    private Department department;
    private Department departmentB;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {

        employeeRepository.deleteAll();
        projectRepository.deleteAll();
        departmentRepository.deleteAll();

        project = new Project();
        project.setName("Project A");
        projectRepository.save(project);

        department = new Department();
        department.setName("IT");
        departmentRepository.save(department);

        departmentB = new Department();
        departmentB.setName("IT B");
        departmentRepository.save(departmentB);

        employee = new Employee();
        employee.setName("John");
        employee.setPosition("Developer");
        employee.setDepartment(departmentB);
        employeeRepository.save(employee);

        employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setName(employee.getName());
        employeeDto.setPosition(employee.getPosition());
        employeeDto.setDepartmentId(department.getId());
        employeeDto.setProjectIds(new HashSet<>(Collections.singletonList(project.getId())));
    }

    @DisplayName("Create Employee API")
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testCreateEmployee() throws Exception {
        given(employeeService.createEmployee(ArgumentMatchers.any(EmployeeDto.class))).willReturn(employeeDto);

        mockMvc.perform(post("/api/employees/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John\", \"position\": \"Developer\", \"departmentId\": 1, \"projectIds\": [1]}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"));
    }

    @DisplayName("Get All Employees API")
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testGetAllEmployees() throws Exception {
        given(employeeService.getAllEmployees()).willReturn(Collections.singletonList(employeeDto));

        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John"));
    }

    @DisplayName("Get Employee by ID API")
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testGetEmployeeById() throws Exception {
        given(employeeService.getEmployeeById(1L)).willReturn(employeeDto);

        mockMvc.perform(get("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"));
    }

    @DisplayName("Update Employee API")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testUpdateEmployee() throws Exception {
        given(employeeService.updateEmployee(ArgumentMatchers.eq(1L), ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(employeeDto);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated John\", \"position\": \"Developer\", \"departmentId\": 1, \"projectIds\": [1]}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"));
    }

    @DisplayName("Delete Employee API")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @DisplayName("Assign Project to Employee API")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testAssignProjectToEmployee() throws Exception {
        given(employeeService.assignProjectToEmployee(employee.getId(), project.getId()))
                .willAnswer(invocation -> {
                    Long employeeId = invocation.getArgument(0);
                    Long projectId = invocation.getArgument(1);
                    EmployeeDto employeeDto = new EmployeeDto();
                    employeeDto.setId(employeeId);
                    employeeDto.setName(employee.getName());
                    employeeDto.setPosition(employee.getPosition());
                    employeeDto.setDepartmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null);
                    employeeDto.setProjectIds(Set.of(projectId));
                    return employeeDto;
                });

        mockMvc.perform(put("/api/employees/{employeeId}/assign-project/{projectId}", employee.getId(), project.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId()))
                .andExpect(jsonPath("$.projectIds").isArray())
                .andExpect(jsonPath("$.projectIds[0]").value(project.getId()));
    }

    @DisplayName("Remove Project to Employee API")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testRemoveProjectFromEmployee() throws Exception {
        given(employeeService.removeProjectFromEmployee(project.getId(), employee.getId()))
                .willAnswer(invocation -> {
                    Long employeeId = invocation.getArgument(1);
                    EmployeeDto employeeDto = new EmployeeDto();
                    employeeDto.setId(employeeId);
                    employeeDto.setName(employee.getName());
                    employeeDto.setPosition(employee.getPosition());
                    employeeDto.setDepartmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null);
                    employeeDto.setProjectIds(Collections.emptySet());
                    return employeeDto;
                });

        mockMvc.perform(delete("/api/employees/{employeeId}/remove-project/{projectId}", employee.getId(), project.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId()))
                .andExpect(jsonPath("$.projectIds").isEmpty());
    }

    @DisplayName("Assign Department to Employee API")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testAssignDepartmentToEmployee() throws Exception {
        given(employeeService.assignDepartmentToEmployee(employee.getId(), department.getId()))
                .willAnswer(invocation -> {
                    Long employeeId = invocation.getArgument(0);
                    Long departmentId = invocation.getArgument(1);
                    EmployeeDto employeeDto = new EmployeeDto();
                    employeeDto.setId(employeeId);
                    employeeDto.setName(employee.getName());
                    employeeDto.setPosition(employee.getPosition());
                    employeeDto.setDepartmentId(departmentId);
                    employeeDto.setProjectIds(employee.getProjects().stream().map(Project::getId).collect(Collectors.toSet()));
                    return employeeDto;
                });

        mockMvc.perform(put("/api/employees/{employeeId}/assign-department/{departmentId}", employee.getId(), department.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId()))
                .andExpect(jsonPath("$.departmentId").value(department.getId()));
    }

    @DisplayName("Remove Department to Employee API")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testRemoveEmployeeFromDepartment() throws Exception {
        given(employeeService.removeEmployeeFromDepartment(employee.getId()))
                .willAnswer(invocation -> {
                    Long emplyeeId = invocation.getArgument(0);
                    EmployeeDto employeeDto = new EmployeeDto();
                    employeeDto.setId(emplyeeId);
                    employeeDto.setName(employee.getName());
                    employeeDto.setPosition(employee.getPosition());
                    employeeDto.setDepartmentId(null);
                    employeeDto.setProjectIds(employee.getProjects().stream().map(Project::getId).collect(Collectors.toSet()));
                    return employeeDto;
                });

        mockMvc.perform(delete("/api/employees/{employeeId}/remove-department", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId()))
                .andExpect(jsonPath("$.departmentId").isEmpty());
    }


}
