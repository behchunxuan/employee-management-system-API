package com.example.employee_management_system_API.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class DepartmentDto {
    private Long id;

    @NotNull(message = "Department name cannot be null")
    @Size(min = 2, message = "Department name must have at least 2 characters")
    private String name;
}
