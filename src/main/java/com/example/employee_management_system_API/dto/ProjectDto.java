package com.example.employee_management_system_API.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class ProjectDto {
    private Long id;

    @NotNull(message = "Project name cannot be null")
    @Size(min = 2, message = "Project name must have at least 2 characters")
    private String name;

    private Set<Long> employeeIds;
}

