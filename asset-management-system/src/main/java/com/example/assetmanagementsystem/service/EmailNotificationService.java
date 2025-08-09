package com.example.assetmanagementsystem.service;

import com.example.assetmanagementsystem.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendNotification(User user, String subject, String message) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            // Log or handle the case where the user has no email address
            System.out.println("User " + user.getUsername() + " has no email address. Cannot send notification.");
            return;
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}
