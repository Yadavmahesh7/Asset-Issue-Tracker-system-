package com.example.assetmanagementsystem;

import org.springframework.web.bind.annotation.GetMapping;
import com.example.assetmanagementsystem.dto.AccessoryDto;
import com.example.assetmanagementsystem.dto.DtoMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accessories")
public class AccessoryController {

    private final AccessoryRepository accessoryRepository;

    public AccessoryController(AccessoryRepository accessoryRepository) {
        this.accessoryRepository = accessoryRepository;
    }

    @GetMapping
    public List<AccessoryDto> getAllAccessories() {
        return accessoryRepository.findAll().stream()
                .map(DtoMapper::toAccessoryDto)
                .collect(Collectors.toList());
    }
}
