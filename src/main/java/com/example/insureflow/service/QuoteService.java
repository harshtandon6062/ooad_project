package com.example.insureflow.service;

import com.example.insureflow.model.Quote;
import com.example.insureflow.model.User;
import com.example.insureflow.patterns.behavioral.PremiumCalculator;
import com.example.insureflow.patterns.creational.QuoteFactory;
import com.example.insureflow.repository.PolicyRepository;
import com.example.insureflow.repository.QuoteRepository;
import com.example.insureflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PremiumCalculator calculator;

    public Quote generateQuote(Long userId, Long policyId, Map<String, Object> userData) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        var policy = policyRepository.findById(policyId).orElseThrow(() -> new RuntimeException("Policy not found"));
        double premium = calculator.calculatePremium(policy.getCoverageAmount(), userData);
        Quote quote = QuoteFactory.createQuote(premium, LocalDate.now().plusDays(30));
        quote.setUser(user);
        quote.setPolicy(policy);
        return quoteRepository.save(quote);
    }

    public List<Quote> getUserQuotes(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return quoteRepository.findByUser(user);
    }

    public Quote getQuote(Long id) {
        return quoteRepository.findById(id).orElseThrow();
    }

    public void deleteQuote(Long id) {
        quoteRepository.deleteById(id);
    }
}