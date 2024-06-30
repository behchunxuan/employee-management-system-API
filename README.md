Employee Management System

Project Description:
The Employee Management System is a Java Spring Boot application designed to manage employees, departments, and projects within a company. It utilizes a relational database and exposes a RESTful API for CRUD operations.

Setup Instructions:
Prerequisites
    -Java 17 or later
    -Maven 3.8+
    -MySQL database

Installation:
    1. Clone the repository
    git clone https://github.com/XXXX/employee-management-system.git
    cd employee-management-system

    2. Configure the MySQL database:
    Create a MySQL database named employee_management.
    Update the application.properties file with your MySQL credentials

    3. Build the application 
    command: mvn clean install

Running the Application:
    1. Run the Spring Boot application (command: mvn spring-boot:run)
    2. Access the application: http://localhost:8080

API Documentation:
Swagger UI
Access the Swagger UI at http://localhost:8080/swagger-ui.html to explore and test the API endpoints interactively.

Authentication:
The API uses basic authentication. Use the following credentials:
    User:
        Username: user
        Password: password
        Role: USER
    Admin:
        Username: admin
        Password: admin
        Role: USER, ADMIN

API Endpoints:
Employee Endpoints
    Create Employee: 
        POST /api/employees/create
        Request Body:
        {
            "name": "User",
            "position": "Senior",
            "departmentId": 1
        }
    Get All Employees: 
        GET /api/employees
    Get Employee by ID: 
        GET /api/employees/{id}
    Update Employee: 
        PUT /api/employees/{id}
        Request Body:
        {
            "name": "Updated User Name",
            "position": "Senior",
            "departmentId": 1
        }
    Delete Employee: 
        DELETE /api/employees/{id}
    Assign Project to Employee: 
        PUT /api/employees/{employeeId}/assign-project/{projectId}
    Remove Project from Employee: 
        DELETE /api/employees/{employeeId}/remove-project/{projectId}
    Assign Department to Employee: 
        PUT /api/employees/{employeeId}/assign-department/{departmentId}
    Remove Employee from Department: 
        DELETE /api/employees/{employeeId}/remove-department

Project Endpoints
    Create Project: 
        POST /api/projects/create
        Request Body:
        {
            "name": "Project Name",
            "description": "Project Description"
        }
    Get All Projects: 
        GET /api/projects
    Get Project by ID: 
        GET /api/projects/{id}
    Update Project: 
        PUT /api/projects/{id}
        Request Body:
        {
            "name": "Updated Project Name",
            "description": "Updated Project Description"
        }
    Delete Project: DELETE /api/projects/{id}

Department Endpoints
    Create Department: POST /api/departments/create
    Request Body:
    {
    "name": "Department Name",
    "description": "Department Description"
    }
    Get All Departments: 
        GET /api/departments
    Get Department by ID: 
        GET /api/departments/{id}
    Update Department: 
        PUT /api/departments/{id}
        Request Body:
        {
            "name": "Updated Department Name",
            "description": "Updated Department Description"
        }
    Delete Department: 
        DELETE /api/departments/{id}

Testing:
    Run unit and integration tests (command: mvn test)
    
