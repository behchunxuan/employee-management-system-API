package com.example.employee_management_system_API.exceptions;

public class DepartmentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 2L;

    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
