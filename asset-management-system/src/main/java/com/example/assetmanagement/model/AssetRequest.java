package com.example.assetmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset_requests")
@Data
public class AssetRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Column(nullable = false)
    private String assetType;

    @Column(nullable = false)
    private int quantity;

    private String justification;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime requestDate;
}
