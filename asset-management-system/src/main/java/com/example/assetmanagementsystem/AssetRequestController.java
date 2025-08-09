package com.example.assetmanagementsystem;

import com.example.assetmanagementsystem.dto.AssetRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.assetmanagementsystem.dto.AssetRequestDto;
import com.example.assetmanagementsystem.dto.AssetRequestResponseDto;
import com.example.assetmanagementsystem.dto.DtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
public class AssetRequestController {

    private final AssetRequestRepository assetRequestRepository;
    private final AccessoryRepository accessoryRepository;
    private final UserRepository userRepository;
    private final com.example.assetmanagementsystem.service.AuditLogService auditLogService;

    public AssetRequestController(AssetRequestRepository assetRequestRepository, AccessoryRepository accessoryRepository, UserRepository userRepository, com.example.assetmanagementsystem.service.AuditLogService auditLogService) {
        this.assetRequestRepository = assetRequestRepository;
        this.accessoryRepository = accessoryRepository;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public List<AssetRequestResponseDto> getMyRequests(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return assetRequestRepository.findByUser(user).stream()
                .map(DtoMapper::toAssetRequestResponseDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<AssetRequestResponseDto> createRequest(@RequestBody AssetRequestDto assetRequestDto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Accessory accessory = accessoryRepository.findById(assetRequestDto.getAccessoryId()).orElseThrow();

        AssetRequest request = new AssetRequest();
        request.setUser(user);
        request.setAccessory(accessory);
        request.setStatus(RequestStatus.PENDING);
        AssetRequest savedRequest = assetRequestRepository.save(request);
        auditLogService.log(user, "ASSET_REQUEST_CREATED", "User " + user.getUsername() + " requested " + accessory.getName());

        return ResponseEntity.ok(DtoMapper.toAssetRequestResponseDto(savedRequest));
    }
}
