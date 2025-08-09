package com.example.assetmanagementsystem;

import com.example.assetmanagementsystem.dto.AssetRequestResponseDto;
import com.example.assetmanagementsystem.dto.DtoMapper;
import com.example.assetmanagementsystem.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store")
@PreAuthorize("hasRole('STORE_MANAGER')")
public class StoreManagerController {

    private final AssetRequestRepository assetRequestRepository;
    private final AccessoryRepository accessoryRepository;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;
    private final com.example.assetmanagementsystem.service.NotificationService notificationService;

    public StoreManagerController(AssetRequestRepository assetRequestRepository, AccessoryRepository accessoryRepository, AuditLogService auditLogService, UserRepository userRepository, com.example.assetmanagementsystem.service.NotificationService notificationService) {
        this.assetRequestRepository = assetRequestRepository;
        this.accessoryRepository = accessoryRepository;
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @PostMapping("/requests/{id}/allocate")
    public ResponseEntity<?> allocateAsset(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        AssetRequest request = assetRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != RequestStatus.APPROVED) {
            return ResponseEntity.badRequest().body("Request is not approved");
        }

        Accessory accessory = request.getAccessory();
        if (accessory.getStock() <= 0) {
            return ResponseEntity.badRequest().body("Asset out of stock");
        }

        accessory.setStock(accessory.getStock() - 1);
        accessoryRepository.save(accessory);

        request.setStatus(RequestStatus.ALLOCATED);
        AssetRequest savedRequest = assetRequestRepository.save(request);

        User storeManager = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        auditLogService.log(storeManager, "ASSET_ALLOCATED", "Store Manager " + storeManager.getUsername() + " allocated " + accessory.getName() + " for request " + request.getId());
        notificationService.sendNotification(request.getUser(), "Asset Allocated", "Your requested asset " + accessory.getName() + " has been allocated to you.");

        return ResponseEntity.ok(DtoMapper.toAssetRequestResponseDto(savedRequest));
    }

    @PostMapping("/assets/{id}/return")
    public ResponseEntity<?> returnAsset(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        AssetRequest request = assetRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Accessory accessory = request.getAccessory();
        accessory.setStock(accessory.getStock() + 1);
        accessoryRepository.save(accessory);

        request.setStatus(RequestStatus.RETURNED);
        AssetRequest savedRequest = assetRequestRepository.save(request);

        User storeManager = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        auditLogService.log(storeManager, "ASSET_RETURNED", "Store Manager " + storeManager.getUsername() + " returned " + accessory.getName() + " for request " + request.getId());
        notificationService.sendNotification(request.getUser(), "Asset Returned", "Your asset " + accessory.getName() + " has been successfully returned.");

        return ResponseEntity.ok(DtoMapper.toAssetRequestResponseDto(savedRequest));
    }
}
