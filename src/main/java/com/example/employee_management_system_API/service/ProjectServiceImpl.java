package com.example.employee_management_system_API.service;

import com.example.employee_management_system_API.dto.ProjectDto;
import com.example.employee_management_system_API.entity.Project;
import com.example.employee_management_system_API.entity.Employee;
import com.example.employee_management_system_API.exceptions.ProjectNotFoundException;
import com.example.employee_management_system_API.repository.ProjectRepository;
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
@CacheConfig(cacheNames = {"projects"})
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = new Project();
        project.setName(projectDto.getName());
        Project savedProject = projectRepository.save(project);
        return mapToDto(savedProject);
    }

    @Override
    @Transactional
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Cacheable(key = "#id")
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        return mapToDto(project);
    }

    @Override
    @Transactional
    @CachePut(key = "#id")
    public ProjectDto updateProject(Long id, ProjectDto projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        project.setName(projectDetails.getName());
        Project updatedProject = projectRepository.save(project);
        return mapToDto(updatedProject);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    private ProjectDto mapToDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setEmployeeIds(project.getEmployees().stream()
                .map(Employee::getId)
                .collect(Collectors.toSet()));
        return projectDto;
    }
}
