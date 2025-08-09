package com.example.assetmanagementsystem.dto;

import com.example.assetmanagementsystem.Accessory;
import com.example.assetmanagementsystem.AssetRequest;

public class DtoMapper {

    public static AccessoryDto toAccessoryDto(Accessory accessory) {
        return new AccessoryDto(
                accessory.getId(),
                accessory.getName(),
                accessory.getStock()
        );
    }

    public static AssetRequestResponseDto toAssetRequestResponseDto(AssetRequest assetRequest) {
        return new AssetRequestResponseDto(
                assetRequest.getId(),
                assetRequest.getUser().getUsername(),
                assetRequest.getAccessory().getName(),
                assetRequest.getStatus()
        );
    }
}
