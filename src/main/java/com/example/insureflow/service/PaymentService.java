package com.example.insureflow.service;

import com.example.insureflow.model.Payment;
import com.example.insureflow.model.Policy;
import com.example.insureflow.model.User;
import com.example.insureflow.repository.PaymentRepository;
import com.example.insureflow.repository.PolicyRepository;
import com.example.insureflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    public Payment initiatePayment(Long userId, Long policyId, BigDecimal amount) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Policy> policy = policyRepository.findById(policyId);

        if (user.isEmpty() || policy.isEmpty()) {
            throw new RuntimeException("User or Policy not found");
        }

        Payment payment = new Payment(user.get(), policy.get(), amount, "card");
        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId);
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

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }
}
