package com.example.insureflow.config;

import com.example.insureflow.model.Policy;
import com.example.insureflow.model.PolicyStatus;
import com.example.insureflow.model.User;
import com.example.insureflow.model.UserRole;
import com.example.insureflow.repository.PolicyRepository;
import com.example.insureflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataLoader {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            // Create demo users
            User admin = new User("Admin User", "admin@insureflow.com", 
                                 passwordEncoder.encode("admin123"), UserRole.ADMIN);
            User customer = new User("John Doe", "john@example.com", 
                                    passwordEncoder.encode("password123"), UserRole.CUSTOMER);
            User adjuster = new User("Jane Smith", "jane@example.com", 
                                     passwordEncoder.encode("password123"), UserRole.CLAIMS_ADJUSTER);
            
            userRepository.save(admin);
            userRepository.save(customer);
            userRepository.save(adjuster);

            // Create policy templates
            Policy healthPolicy = new Policy("health", "MediCare Insurance", 
                new BigDecimal("250.00"), new BigDecimal("100000.00"));
            healthPolicy.setStatus(PolicyStatus.TEMPLATE);

            Policy autoPolicy = new Policy("auto", "DriveGuard Insurance", 
                new BigDecimal("500.00"), new BigDecimal("500000.00"));
            autoPolicy.setStatus(PolicyStatus.TEMPLATE);

            Policy homePolicy = new Policy("home", "homeShield Insurance", 
                new BigDecimal("800.00"), new BigDecimal("1000000.00"));
            homePolicy.setStatus(PolicyStatus.TEMPLATE);

            Policy lifePolicy = new Policy("life", "LifeSecure Insurance", 
                new BigDecimal("300.00"), new BigDecimal("2000000.00"));
            lifePolicy.setStatus(PolicyStatus.TEMPLATE);

            policyRepository.save(healthPolicy);
            policyRepository.save(autoPolicy);
            policyRepository.save(homePolicy);
            policyRepository.save(lifePolicy);

            System.out.println("✓ Demo data loaded successfully!");
        };
    }
}
