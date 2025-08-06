package com.example.assetmanagement.dto;

import lombok.Data;

@Data
public class AssetRequestDto {
    private String assetType;
    private int quantity;
    private String justification;
}
