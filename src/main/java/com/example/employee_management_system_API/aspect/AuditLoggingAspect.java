package com.example.employee_management_system_API.aspect;

import com.example.employee_management_system_API.dto.EmployeeDto;
import com.example.employee_management_system_API.dto.DepartmentDto;
import com.example.employee_management_system_API.dto.ProjectDto;
import com.example.employee_management_system_API.service.AuditLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class AuditLoggingAspect {
    private final AuditLogService auditLogService;

    @Autowired
    public AuditLoggingAspect(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    // Pointcut for read methods
    @Pointcut("execution(* com.example.employee_management_system_API.service.*.get*(..))")
    private void entityReadMethods() {}

    // Pointcut for create methods
    @Pointcut("execution(* com.example.employee_management_system_API.service.*.create*(..))")
    private void entityCreateMethods() {}

    // Pointcut for update methods
    @Pointcut("execution(* com.example.employee_management_system_API.service.*.update*(..))")
    private void entityUpdateMethods() {}

    // Pointcut for delete methods
    @Pointcut("execution(* com.example.employee_management_system_API.service.*.delete*(..))")
    private void entityDeleteMethods() {}

    // Pointcut for assigning and removing actions
    @Pointcut("execution(* com.example.employee_management_system_API.service.EmployeeServiceImpl.assign*(..)) " +
            "|| execution(* com.example.employee_management_system_API.service.EmployeeServiceImpl.remove*(..))")
    private void assignOrRemoveMethods() {}

    // Advice to log after read operations
    @AfterReturning(pointcut = "entityReadMethods()", returning = "result")
    public void logAfterReadServiceMethodExecution(Object result) {
        if (result instanceof List) {
            List<?> entities = (List<?>) result;
            if (!entities.isEmpty()) {
                Object firstEntity = entities.get(0);
                if (firstEntity instanceof EmployeeDto) {
                    entities.forEach(entity -> {
                        EmployeeDto employee = (EmployeeDto) entity;
                        auditLogService.logAction("READ", "Employee", employee.getId());
                    });
                } else if (firstEntity instanceof DepartmentDto) {
                    entities.forEach(entity -> {
                        DepartmentDto department = (DepartmentDto) entity;
                        auditLogService.logAction("READ", "Department", department.getId());
                    });
                } else if (firstEntity instanceof ProjectDto) {
                    entities.forEach(entity -> {
                        ProjectDto project = (ProjectDto) entity;
                        auditLogService.logAction("READ", "Project", project.getId());
                    });
                }
            }
        } else if (result instanceof EmployeeDto) {
            EmployeeDto employee = (EmployeeDto) result;
            auditLogService.logAction("READ", "Employee", employee.getId());
        } else if (result instanceof DepartmentDto) {
            DepartmentDto department = (DepartmentDto) result;
            auditLogService.logAction("READ", "Department", department.getId());
        } else if (result instanceof ProjectDto) {
            ProjectDto project = (ProjectDto) result;
            auditLogService.logAction("READ", "Project", project.getId());
        }
    }

    // Advice to log after create operations
    @AfterReturning(pointcut = "entityCreateMethods()", returning = "result")
    public void logAfterCreateServiceMethodExecution(Object result) {
        if (result instanceof EmployeeDto) {
            EmployeeDto employee = (EmployeeDto) result;
            auditLogService.logAction("CREATE", "Employee", employee.getId());
        } else if (result instanceof DepartmentDto) {
            DepartmentDto department = (DepartmentDto) result;
            auditLogService.logAction("CREATE", "Department", department.getId());
        } else if (result instanceof ProjectDto) {
            ProjectDto project = (ProjectDto) result;
            auditLogService.logAction("CREATE", "Project", project.getId());
        }
    }

    // Advice to log after update operations
    @AfterReturning(pointcut = "entityUpdateMethods()", returning = "result")
    public void logAfterUpdateServiceMethodExecution(Object result) {
        if (result instanceof EmployeeDto) {
            EmployeeDto employee = (EmployeeDto) result;
            auditLogService.logAction("UPDATE", "Employee", employee.getId());
        } else if (result instanceof DepartmentDto) {
            DepartmentDto department = (DepartmentDto) result;
            auditLogService.logAction("UPDATE", "Department", department.getId());
        } else if (result instanceof ProjectDto) {
            ProjectDto project = (ProjectDto) result;
            auditLogService.logAction("UPDATE", "Project", project.getId());
        }
    }

    @Before("entityDeleteMethods() && args(id)")
    public void logBeforeDeleteServiceMethodExecution(JoinPoint joinPoint, Long id) {
        String entityName = joinPoint.getSignature().getDeclaringTypeName();
        if (entityName.contains("EmployeeService")) {
            auditLogService.logAction("DELETE", "Employee", id);
        } else if (entityName.contains("DepartmentService")) {
            auditLogService.logAction("DELETE", "Department", id);
        } else if (entityName.contains("ProjectService")) {
            auditLogService.logAction("DELETE", "Project", id);
        }
    }

    @AfterReturning(pointcut = "assignOrRemoveMethods() && args(employeeId, ..)", returning = "result")
    public void logAssignOrRemoveExecution(JoinPoint joinPoint, Long employeeId, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String actionType = methodName.startsWith("assign") ? "ASSIGN" : "REMOVE";
        String entityType = null;

        if (methodName.contains("Project")) {
            entityType = "Project";
        } else if (methodName.contains("Department")) {
            entityType = "Department";
        } else {
            System.out.println("Unknown entity type for method: " + methodName);
            return;
        }

        auditLogService.logAction(actionType + "_" + entityType.toUpperCase(), entityType, employeeId);
    }

}
