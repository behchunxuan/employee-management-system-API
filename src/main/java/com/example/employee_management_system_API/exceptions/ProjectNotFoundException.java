package com.example.employee_management_system_API.exceptions;

public class ProjectNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3L;

    public ProjectNotFoundException(String message) {
        super(message);
    }
}
