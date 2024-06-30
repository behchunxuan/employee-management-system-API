package com.example.employee_management_system_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
//@EnableSwagger2
public class EmployeeManagementSystemApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementSystemApiApplication.class, args);
	}
}
