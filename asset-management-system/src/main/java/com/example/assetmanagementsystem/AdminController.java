package com.example.assetmanagementsystem;

import com.example.assetmanagementsystem.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.assetmanagementsystem.dto.AssetRequestResponseDto;
import com.example.assetmanagementsystem.dto.DtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AssetRequestRepository assetRequestRepository;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;
    private final com.example.assetmanagementsystem.service.NotificationService notificationService;

    public AdminController(AssetRequestRepository assetRequestRepository, AuditLogService auditLogService, UserRepository userRepository, com.example.assetmanagementsystem.service.NotificationService notificationService) {
        this.assetRequestRepository = assetRequestRepository;
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @GetMapping("/requests")
    public List<AssetRequestResponseDto> getAllRequests() {
        return assetRequestRepository.findAll().stream()
                .map(DtoMapper::toAssetRequestResponseDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/requests/{id}/approve")
    public ResponseEntity<AssetRequestResponseDto> approveRequest(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        AssetRequest request = assetRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(RequestStatus.APPROVED);
        AssetRequest savedRequest = assetRequestRepository.save(request);

        User admin = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        auditLogService.log(admin, "ASSET_REQUEST_APPROVED", "Admin " + admin.getUsername() + " approved request " + request.getId());
        notificationService.sendNotification(request.getUser(), "Asset Request Approved", "Your request for " + request.getAccessory().getName() + " has been approved.");

        return ResponseEntity.ok(DtoMapper.toAssetRequestResponseDto(savedRequest));
    }

    @PostMapping("/requests/{id}/reject")
    public ResponseEntity<AssetRequestResponseDto> rejectRequest(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        AssetRequest request = assetRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(RequestStatus.DENIED);
        AssetRequest savedRequest = assetRequestRepository.save(request);

        User admin = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        auditLogService.log(admin, "ASSET_REQUEST_REJECTED", "Admin " + admin.getUsername() + " rejected request " + request.getId());
        notificationService.sendNotification(request.getUser(), "Asset Request Rejected", "Your request for " + request.getAccessory().getName() + " has been rejected.");

        return ResponseEntity.ok(DtoMapper.toAssetRequestResponseDto(savedRequest));
    }
}
