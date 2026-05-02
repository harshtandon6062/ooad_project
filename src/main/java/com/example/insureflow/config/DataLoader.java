package com.example.insureflow.config;

import com.example.insureflow.model.UserRole;
import com.example.insureflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        try {
            userService.registerUser("Harsh Customer", "harsh@example.com", "password123", UserRole.CUSTOMER);
            userService.registerUser("Priya Agent", "priya@example.com", "password123", UserRole.AGENT);
            userService.registerUser("Admin", "admin@example.com", "admin123", UserRole.ADMIN);
            userService.registerUser("Raj Adjuster", "raj@example.com", "password123", UserRole.CLAIMS_ADJUSTER);
        } catch (Exception e) {
            // users already exist
        }
    }
}