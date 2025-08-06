package com.example.assetmanagement.controller;

import com.example.assetmanagement.dto.AssetRequestDto;
import com.example.assetmanagement.model.AssetRequest;
import com.example.assetmanagement.service.AssetRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/asset")
public class AssetRequestController {

    @Autowired
    private AssetRequestService assetRequestService;

    @PostMapping("/request")
    public ResponseEntity<AssetRequest> requestAsset(@RequestBody AssetRequestDto assetRequestDto) {
        AssetRequest createdRequest = assetRequestService.createAssetRequest(assetRequestDto);
        return ResponseEntity.ok(createdRequest);
    }
}
