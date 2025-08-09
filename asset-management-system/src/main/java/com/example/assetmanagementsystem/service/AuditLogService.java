package com.example.assetmanagementsystem.service;

import com.example.assetmanagementsystem.AuditLog;
import com.example.assetmanagementsystem.AuditLogRepository;
import com.example.assetmanagementsystem.User;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(User user, String action, String details) {
        auditLogRepository.save(new AuditLog(user, action, details));
    }
}
