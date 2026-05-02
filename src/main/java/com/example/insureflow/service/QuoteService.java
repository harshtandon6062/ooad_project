package com.example.insureflow.service;

import com.example.insureflow.model.Quote;
import com.example.insureflow.model.Policy;
import com.example.insureflow.model.User;
import com.example.insureflow.repository.QuoteRepository;
import com.example.insureflow.repository.PolicyRepository;
import com.example.insureflow.repository.UserRepository;
import com.example.insureflow.patterns.behavioral.StandardPremiumStrategy;
import com.example.insureflow.patterns.behavioral.PremiumCalculatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuoteService {
    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private UserRepository userRepository;

    public Quote generateQuote(Long userId, Long policyId, Integer age) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Policy> policy = policyRepository.findById(policyId);

        if (user.isEmpty() || policy.isEmpty()) {
            throw new RuntimeException("User or Policy not found");
        }

        // Calculate premium using Strategy Pattern
        Map<String, Object> userData = new HashMap<>();
        if (age != null) {
            userData.put("age", age);
        }
        
        PremiumCalculatorImpl calculator = new PremiumCalculatorImpl(new StandardPremiumStrategy());
        BigDecimal premiumEstimate = calculator.calculatePremium(policy.get().getPremium(), userData);

        LocalDate validUntil = LocalDate.now().plusDays(7);
        Quote quote = new Quote(user.get(), policy.get(), premiumEstimate, validUntil);
        return quoteRepository.save(quote);
    }

    public Optional<Quote> getQuoteById(Long quoteId) {
        return quoteRepository.findById(quoteId);
    }

    public List<Quote> getUserQuotes(Long userId) {
        return quoteRepository.findByUserId(userId);
    }

    public Quote save(Quote quote) {
        return quoteRepository.save(quote);
    }
}
