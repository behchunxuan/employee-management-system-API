package com.example.employee_management_system_API.integrationTest;

import com.example.employee_management_system_API.controller.ProjectController;
import com.example.employee_management_system_API.dto.EmployeeDto;
import com.example.employee_management_system_API.dto.ProjectDto;
import com.example.employee_management_system_API.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    private ProjectDto projectDto;

    @BeforeEach
    public void setUp() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setName("John");

        projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setName("Project 1");
        projectDto.setEmployeeIds(Collections.singleton(employeeDto.getId()));
    }

    @DisplayName("Create Project API")
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testCreateProject() throws Exception {
        given(projectService.createProject(ArgumentMatchers.any(ProjectDto.class))).willReturn(projectDto);

        mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Project 1\", \"employeeIds\": [1]}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Project 1"));
    }

    @DisplayName("Get All Projects API")
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testGetAllProjects() throws Exception {
        given(projectService.getAllProjects()).willReturn(Collections.singletonList(projectDto));

        mockMvc.perform(get("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Project 1"));
    }

    @DisplayName("Get Project by ID API")
    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void testGetProjectById() throws Exception {
        given(projectService.getProjectById(1L)).willReturn(projectDto);

        mockMvc.perform(get("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Project 1"));
    }

    @DisplayName("Update Project API")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testUpdateProject() throws Exception {
        ProjectDto updatedProjectDto = new ProjectDto();
        updatedProjectDto.setId(1L);
        updatedProjectDto.setName("Updated Project 1");
        updatedProjectDto.setEmployeeIds(Collections.singleton(1L));

        given(projectService.updateProject(ArgumentMatchers.eq(1L), ArgumentMatchers.any(ProjectDto.class)))
                .willReturn(updatedProjectDto);

        mockMvc.perform(put("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Project 1\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Project 1"));
    }

    @DisplayName("Delete Project API")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testDeleteProject() throws Exception {
        mockMvc.perform(delete("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
