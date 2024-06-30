package com.example.employee_management_system_API.unitTest;

import com.example.employee_management_system_API.dto.ProjectDto;
import com.example.employee_management_system_API.entity.Project;
import com.example.employee_management_system_API.exceptions.ProjectNotFoundException;
import com.example.employee_management_system_API.repository.ProjectRepository;
import com.example.employee_management_system_API.service.ProjectServiceImpl;
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
public class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project project;
    private ProjectDto projectDto;

    @BeforeEach
    public void setUp() {
        project = new Project();
        project.setId(1L);
        project.setName("Project A");

        projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setName("Project A");
    }

    @DisplayName("Create a project")
    @Test
    public void testCreateProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectDto result = projectService.createProject(projectDto);

        assertNotNull(result);
        assertEquals("Project A", result.getName());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @DisplayName("Retrieve all projects")
    @Test
    public void testGetAllProjects() {
        List<Project> projects = Arrays.asList(new Project(1L, "Project A"), new Project(2L, "Project B"), new Project(3L, "Project C"));
        when(projectRepository.findAll()).thenReturn(projects);

        List<ProjectDto> result = projectService.getAllProjects();

        assertEquals(3, result.size());
        assertEquals("Project A", result.get(0).getName());
        assertEquals("Project B", result.get(1).getName());
        assertEquals("Project C", result.get(2).getName());
    }

    @DisplayName("Retrieve a project by ID")
    @Test
    public void testGetProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectDto result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals("Project A", result.getName());
    }

    @DisplayName("Retrieve a project by ID - Not Found")
    @Test
    public void testGetProjectById_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectById(1L));
    }

    @DisplayName("Update a project")
    @Test
    public void testUpdateProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectDto updatedDto = new ProjectDto();
        updatedDto.setName("Project B");

        ProjectDto result = projectService.updateProject(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Project B", result.getName());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @DisplayName("Delete a project")
    @Test
    public void testDeleteProject() {
        doNothing().when(projectRepository).deleteById(1L);

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }
}