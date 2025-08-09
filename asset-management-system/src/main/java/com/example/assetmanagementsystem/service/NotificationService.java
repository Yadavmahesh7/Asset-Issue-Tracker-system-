package com.example.assetmanagementsystem.service;

import com.example.assetmanagementsystem.User;

public interface NotificationService {
    void sendNotification(User user, String subject, String message);
}
