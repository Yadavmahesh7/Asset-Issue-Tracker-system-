package com.example.assetmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "assets")
@Data
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String assetType;

    @Column(unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private String status;
}
