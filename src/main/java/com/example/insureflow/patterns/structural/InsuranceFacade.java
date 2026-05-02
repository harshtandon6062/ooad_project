package com.example.insureflow.patterns.structural;

import com.example.insureflow.model.*;
import com.example.insureflow.repository.*;
import com.example.insureflow.patterns.creational.PolicyFactory;
import com.example.insureflow.patterns.creational.QuoteFactory;
import com.example.insureflow.patterns.creational.ClaimFactory;
import com.example.insureflow.patterns.behavioral.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * FACADE PATTERN (Structural)
 * Purpose: Provides unified interface to the complex insurance subsystem
 */
public class InsuranceFacade {
    private final UserRepository userRepository;
    private final PolicyRepository policyRepository;
    private final QuoteRepository quoteRepository;
    private final ClaimRepository claimRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    public InsuranceFacade(UserRepository userRepository, PolicyRepository policyRepository,
                          QuoteRepository quoteRepository, ClaimRepository claimRepository,
                          PaymentRepository paymentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.policyRepository = policyRepository;
        this.quoteRepository = quoteRepository;
        this.claimRepository = claimRepository;
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==================== USER MANAGEMENT ====================

    public User registerUser(String name, String email, String password, UserRole role) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User(name, email, passwordEncoder.encode(password), role);
        return userRepository.save(user);
    }

    public Optional<User> authenticate(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    // ==================== POLICY OPERATIONS ====================

    public List<Policy> searchPolicies() {
        return policyRepository.findByStatus(PolicyStatus.TEMPLATE);
    }

    public List<Policy> searchPoliciesByType(String type) {
        return policyRepository.findByStatus(PolicyStatus.TEMPLATE).stream()
            .filter(p -> p.getType().equalsIgnoreCase(type))
            .toList();
    }

    public Optional<Policy> getPolicyDetails(Long policyId) {
        return policyRepository.findById(policyId);
    }

    public List<Policy> getUserPolicies(Long userId) {
        return policyRepository.findByUserIdAndStatusNot(userId, PolicyStatus.TEMPLATE);
    }

    public Policy createPolicyTemplate(String type, String provider, BigDecimal premium, BigDecimal coverageAmount) {
        Policy policy = PolicyFactory.createTemplate(type, provider, premium, coverageAmount);
        return policyRepository.save(policy);
    }

    public Policy purchasePolicy(Long userId, Long policyTemplateId, LocalDate startDate, LocalDate expiryDate) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Policy> template = policyRepository.findById(policyTemplateId);
        
        if (user.isEmpty() || template.isEmpty()) {
            throw new IllegalArgumentException("User or Policy not found");
        }

        Policy policy = PolicyFactory.issuePolicy(
            template.get().getType(),
            template.get().getProvider(),
            template.get().getPremium(),
            template.get().getCoverageAmount(),
            startDate,
            expiryDate
        );
        policy.setUser(user.get());
        return policyRepository.save(policy);
    }

    // ==================== QUOTE OPERATIONS ====================

    public Quote generateQuote(Long userId, Long policyId, Integer age) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Policy> policy = policyRepository.findById(policyId);

        if (user.isEmpty() || policy.isEmpty()) {
            throw new IllegalArgumentException("User or Policy not found");
        }

        // Calculate premium using Strategy Pattern
        Map<String, Object> userData = new HashMap<>();
        if (age != null) {
            userData.put("age", age);
        }
        
        PremiumStrategy strategy = new StandardPremiumStrategy();
        PremiumCalculatorImpl calculator = new PremiumCalculatorImpl(strategy);
        BigDecimal premiumEstimate = calculator.calculatePremium(policy.get().getPremium(), userData);

        LocalDate validUntil = LocalDate.now().plusDays(7);
        Quote quote = QuoteFactory.createQuote(user.get(), policy.get(), premiumEstimate, validUntil);
        return quoteRepository.save(quote);
    }

    public Optional<Quote> getQuote(Long quoteId) {
        return quoteRepository.findById(quoteId);
    }

    public List<Quote> getUserQuotes(Long userId) {
        return quoteRepository.findByUserId(userId);
    }

    // ==================== CLAIM OPERATIONS ====================

    public Claim submitClaim(Long policyId, Long userId, BigDecimal amount, String description) {
        Optional<Policy> policy = policyRepository.findById(policyId);
        Optional<User> user = userRepository.findById(userId);

        if (policy.isEmpty() || user.isEmpty()) {
            throw new IllegalArgumentException("Policy or User not found");
        }

        Claim claim = ClaimFactory.createClaim(policy.get(), user.get(), amount, description);
        return claimRepository.save(claim);
    }

    public Optional<Claim> getClaimDetails(Long claimId) {
        return claimRepository.findById(claimId);
    }

    public List<Claim> getUserClaims(Long userId) {
        return claimRepository.findByUserId(userId);
    }

    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    public void updateClaimStatus(Long claimId, ClaimStatus status) {
        Optional<Claim> claim = claimRepository.findById(claimId);
        if (claim.isPresent()) {
            claim.get().setStatus(status);
            claimRepository.save(claim.get());
        }
    }

    // ==================== PAYMENT OPERATIONS ====================

    public Payment initiatePayment(Long userId, Long policyId, BigDecimal amount) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Policy> policy = policyRepository.findById(policyId);

        if (user.isEmpty() || policy.isEmpty()) {
            throw new IllegalArgumentException("User or Policy not found");
        }

        Payment payment = new Payment(user.get(), policy.get(), amount, "card");
        return paymentRepository.save(payment);
    }

    public void processPayment(Long paymentId, String status) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (payment.isPresent()) {
            payment.get().setStatus(status);
            paymentRepository.save(payment.get());
        }
    }

    public List<Payment> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    // ==================== DASHBOARD STATISTICS ====================

    public Map<String, Object> getUserDashboard(Long userId) {
        Map<String, Object> data = new HashMap<>();
        List<Policy> policies = getUserPolicies(userId);
        List<Claim> claims = getUserClaims(userId);
        List<Quote> quotes = getUserQuotes(userId);

        data.put("activePoliciesCount", policies.stream()
            .filter(p -> p.getStatus() == PolicyStatus.ACTIVE).count());
        data.put("pendingClaimsCount", claims.stream()
            .filter(c -> c.getStatus() == ClaimStatus.SUBMITTED).count());
        data.put("totalPolicies", policies.size());
        data.put("policies", policies);
        data.put("claims", claims);
        data.put("quotes", quotes);

        return data;
    }
}
