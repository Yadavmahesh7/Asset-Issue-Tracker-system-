package com.example.assetmanagementsystem.dto;

import com.example.assetmanagementsystem.RequestStatus;

public class AssetRequestResponseDto {
    private Long id;
    private String username;
    private String accessoryName;
    private RequestStatus status;

    public AssetRequestResponseDto(Long id, String username, String accessoryName, RequestStatus status) {
        this.id = id;
        this.username = username;
        this.accessoryName = accessoryName;
        this.status = status;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessoryName() {
        return accessoryName;
    }

    public void setAccessoryName(String accessoryName) {
        this.accessoryName = accessoryName;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
