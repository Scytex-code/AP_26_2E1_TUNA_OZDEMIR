package com.example.quiz.server.service;

import com.example.quiz.server.entity.AuditLogEntity;
import com.example.quiz.server.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {
    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(String action, String entityName, Long entityId, String details) {
        repository.save(new AuditLogEntity(action, entityName, entityId, details));
    }
}
