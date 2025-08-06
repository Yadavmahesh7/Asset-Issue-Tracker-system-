package com.example.assetmanagement.service;

import com.example.assetmanagement.dto.AssetRequestDto;
import com.example.assetmanagement.model.AssetRequest;
import com.example.assetmanagement.model.User;
import com.example.assetmanagement.repository.AssetRequestRepository;
import com.example.assetmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AssetRequestService {

    @Autowired
    private AssetRequestRepository assetRequestRepository;

    @Autowired
    private UserRepository userRepository;

    public AssetRequest createAssetRequest(AssetRequestDto assetRequestDto) {
        // For now, we'll create a dummy user or fetch a default one.
        // In a real app, this would come from the security context.
        User employee = userRepository.findByUsername("employee");
        if (employee == null) {
            // Create a dummy user for now if one doesn't exist
            employee = new User();
            employee.setUsername("employee");
            employee.setPassword("password"); // Should be encoded in a real app
            employee.setEmail("employee@example.com");
            userRepository.save(employee);
        }


        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setEmployee(employee);
        assetRequest.setAssetType(assetRequestDto.getAssetType());
        assetRequest.setQuantity(assetRequestDto.getQuantity());
        assetRequest.setJustification(assetRequestDto.getJustification());
        assetRequest.setStatus("PENDING");
        assetRequest.setRequestDate(LocalDateTime.now());

        return assetRequestRepository.save(assetRequest);
    }
}
