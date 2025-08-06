package com.example.assetmanagement.model;

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
    private String actionType;

    @Column(nullable = false)
    private String performedBy;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Lob
    private String details;
}
