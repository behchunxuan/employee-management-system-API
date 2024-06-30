package com.example.employee_management_system_API.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Employee Management API",
                description = "REST API for managing employees, departments, and projects.",
                version = "1.0.0",
                contact = @Contact(
                        name = "ChunXuan",
                        email = "chunxuan2002@gmail.com"
                ),
                license = @License(
                        name = "Apache License 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"
                )
        )
)
public class OpenApiConfig {
}
