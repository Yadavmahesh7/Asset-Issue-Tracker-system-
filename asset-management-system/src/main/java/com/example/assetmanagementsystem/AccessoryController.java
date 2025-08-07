package com.example.assetmanagementsystem;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccessoryController {

    private final AccessoryRepository accessoryRepository;

    public AccessoryController(AccessoryRepository accessoryRepository) {
        this.accessoryRepository = accessoryRepository;
    }

    @GetMapping("/accessories")
    @PreAuthorize("hasRole('ADMIN')")
    public String listAccessories(Model model) {
        model.addAttribute("accessories", accessoryRepository.findAll());
        return "accessories";
    }

    @PostMapping("/accessories")
    @PreAuthorize("hasRole('ADMIN')")
    public String addAccessory(@RequestParam String name, @RequestParam int stock) {
        Accessory accessory = new Accessory();
        accessory.setName(name);
        accessory.setStock(stock);
        accessoryRepository.save(accessory);
        return "redirect:/accessories";
    }
}
