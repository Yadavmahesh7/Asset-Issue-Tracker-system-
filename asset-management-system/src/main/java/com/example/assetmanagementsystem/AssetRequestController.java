package com.example.assetmanagementsystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AssetRequestController {

    private static final Logger logger = LoggerFactory.getLogger(AssetRequestController.class);

    private final AssetRequestRepository assetRequestRepository;
    private final AccessoryRepository accessoryRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public AssetRequestController(AssetRequestRepository assetRequestRepository, AccessoryRepository accessoryRepository, UserRepository userRepository, EmailService emailService) {
        this.assetRequestRepository = assetRequestRepository;
        this.accessoryRepository = accessoryRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @GetMapping("/requests")
    public String listRequests(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        if (user.getRoles().contains(Role.ADMIN)) {
            model.addAttribute("requests", assetRequestRepository.findAll());
        } else {
            model.addAttribute("requests", assetRequestRepository.findAll().stream().filter(r -> r.getUser().equals(user)).toList());
        }
        model.addAttribute("accessories", accessoryRepository.findAll());
        return "requests";
    }

    @PostMapping("/requests")
    public String createRequest(@RequestParam Long accessoryId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Accessory accessory = accessoryRepository.findById(accessoryId).orElseThrow();

        AssetRequest request = new AssetRequest();
        request.setUser(user);
        request.setAccessory(accessory);
        request.setStatus(RequestStatus.PENDING);
        assetRequestRepository.save(request);

        return "redirect:/requests";
    }

    @PostMapping("/requests/{id}/approve")
    public String approveRequest(@PathVariable Long id) {
        AssetRequest request = assetRequestRepository.findById(id).orElseThrow();
        request.setStatus(RequestStatus.APPROVED);
        Accessory accessory = request.getAccessory();
        accessory.setStock(accessory.getStock() - 1);
        assetRequestRepository.save(request);
        accessoryRepository.save(accessory);
        logger.info("Asset request {} for {} approved. Allocated {} to user {}.",
                request.getId(), accessory.getName(), accessory.getName(), request.getUser().getUsername());
        return "redirect:/requests";
    }

    @PostMapping("/requests/{id}/deny")
    public String denyRequest(@PathVariable Long id) {
        AssetRequest request = assetRequestRepository.findById(id).orElseThrow();
        request.setStatus(RequestStatus.DENIED);
        assetRequestRepository.save(request);
        // In a real application, you would get the user's email from the User object
        emailService.sendSimpleMessage("user-email@example.com", "Asset Request Denied",
                "Your request for " + request.getAccessory().getName() + " has been denied.");
        return "redirect:/requests";
    }
}
