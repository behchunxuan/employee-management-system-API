package com.example.employee_management_system_API.integrationTest;

import com.example.employee_management_system_API.entity.Department;
import com.example.employee_management_system_API.repository.DepartmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DepartmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Department department;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        departmentRepository.deleteAll();

        department = new Department();
        department.setName("HR");
        department = departmentRepository.save(department);
    }

    @DisplayName("Get All Departments API")
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testGetAllDepartments() throws Exception {
        mockMvc.perform(get("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("HR"));
    }

    @DisplayName("Get Department by ID API")
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testGetDepartmentById() throws Exception {
        mockMvc.perform(get("/api/departments/{id}", department.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HR"));
    }

    @DisplayName("Create Department API")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testCreateDepartment() throws Exception {
        Department newDepartment = new Department();
        newDepartment.setName("IT");

        mockMvc.perform(post("/api/departments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newDepartment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("IT"));
    }

    @DisplayName("Update Department API")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testUpdateDepartment() throws Exception {
        department.setName("Updated HR");

        mockMvc.perform(put("/api/departments/{id}", department.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(department)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated HR"));
    }

    @DisplayName("Delete Department API")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testDeleteDepartment() throws Exception {
        mockMvc.perform(delete("/api/departments/{id}", department.getId()))
                .andExpect(status().isNoContent());
    }
}
