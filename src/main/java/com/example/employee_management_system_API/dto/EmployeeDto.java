package com.example.employee_management_system_API.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Data
public class EmployeeDto {
    private Long id;

    @NotNull(message = "Employee name cannot be null")
    @Size(min = 2, message = "Employee name must have at least 2 characters")
    private String name;

    @NotNull(message = "Position cannot be null")
    @Size(min = 2, message = "Position must have at least 2 characters")
    private String position;

//    @NotNull(message = "Department ID cannot be null")
    private Long departmentId;

    private Set<Long> projectIds;
}
