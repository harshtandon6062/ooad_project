package com.example.insureflow.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotes")
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quoteId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "premium_estimate", nullable = false)
    private BigDecimal premiumEstimate;

    @Column(name = "valid_until")
    private LocalDate validUntil;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Quote() {}

    public Quote(User user, Policy policy, BigDecimal premiumEstimate, LocalDate validUntil) {
        this.user = user;
        this.policy = policy;
        this.premiumEstimate = premiumEstimate;
        this.validUntil = validUntil;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getQuoteId() { return quoteId; }
    public void setQuoteId(Long quoteId) { this.quoteId = quoteId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Policy getPolicy() { return policy; }
    public void setPolicy(Policy policy) { this.policy = policy; }
    public BigDecimal getPremiumEstimate() { return premiumEstimate; }
    public void setPremiumEstimate(BigDecimal premiumEstimate) { this.premiumEstimate = premiumEstimate; }
    public LocalDate getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
