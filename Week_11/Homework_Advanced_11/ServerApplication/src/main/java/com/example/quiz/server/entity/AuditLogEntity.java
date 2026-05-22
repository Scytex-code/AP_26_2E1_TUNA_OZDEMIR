package com.example.quiz.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "audit_logs")
public class AuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String action;
    private String entityName;
    private Long entityId;
    private String details;
    private Instant createdAt = Instant.now();

    protected AuditLogEntity() {
    }

    public AuditLogEntity(String action, String entityName, Long entityId, String details) {
        this.action = action;
        this.entityName = entityName;
        this.entityId = entityId;
        this.details = details;
    }
}
