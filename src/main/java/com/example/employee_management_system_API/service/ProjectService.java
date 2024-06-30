package com.example.employee_management_system_API.service;

import com.example.employee_management_system_API.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    ProjectDto createProject(ProjectDto projectDto);
    List<ProjectDto> getAllProjects();
    ProjectDto getProjectById(Long id);
    ProjectDto updateProject(Long id, ProjectDto projectDetails);
    void deleteProject(Long id);
}
