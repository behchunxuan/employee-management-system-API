package com.example.employee_management_system_API.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String entity;

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public AuditLog() {
    }

    public AuditLog(Long id, String action, String entity, Long entityId, LocalDateTime timestamp) {
        this.id = id;
        this.action = action;
        this.entity = entity;
        this.entityId = entityId;
        this.timestamp = timestamp;
    }
}
