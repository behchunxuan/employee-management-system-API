package com.example.employee_management_system_API.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorObject {
    private Integer statusCode;
    private String message;
    private Date timestamp;

    public ErrorObject(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = new Date();
    }
}

