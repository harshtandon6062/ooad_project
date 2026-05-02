package com.example.insureflow.controller;

import com.example.insureflow.model.Policy;
import com.example.insureflow.model.Quote;
import com.example.insureflow.model.User;
import com.example.insureflow.service.PolicyService;
import com.example.insureflow.service.QuoteService;
import com.example.insureflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/policies")
public class PolicyController {
    @Autowired
    private PolicyService policyService;

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String searchPolicies(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        List<Policy> policies = policyService.searchTemplates();
        User user = (User) session.getAttribute("user");
        model.addAttribute("policies", policies);
        model.addAttribute("user", user);
        return "policies";
    }

    @GetMapping("/{id}")
    public String getPolicyDetails(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Optional<Policy> policy = policyService.getPolicyById(id);
        if (policy.isPresent()) {
            User user = (User) session.getAttribute("user");
            model.addAttribute("policy", policy.get());
            model.addAttribute("user", user);
            return "policy-detail";
        }
        return "error";
    }

    @PostMapping("/{id}/quote")
    public String generateQuote(@PathVariable Long id, @RequestParam Integer age,
                               HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        try {
            Quote quote = quoteService.generateQuote(userId, id, age);
            Optional<Policy> policy = policyService.getPolicyById(id);
            User user = (User) session.getAttribute("user");
            model.addAttribute("quote", quote);
            model.addAttribute("policy", policy.get());
            model.addAttribute("user", user);
            return "quote";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/my-policies")
    public String myPolicies(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        List<Policy> policies = policyService.getUserPolicies(userId);
        User user = (User) session.getAttribute("user");
        model.addAttribute("policies", policies);
        model.addAttribute("user", user);
        return "my-policies";
    }

    @GetMapping("/admin/policies")
    public String adminPolicies(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("user");
        if (!user.getRole().name().equals("ADMIN")) {
            return "redirect:/dashboard";
        }

        List<Policy> policies = policyService.searchTemplates();
        model.addAttribute("policies", policies);
        model.addAttribute("user", user);
        return "admin-policies";
    }

    @PostMapping("/admin/create")
    public String createTemplate(@RequestParam String type, @RequestParam String provider,
                                @RequestParam BigDecimal premium, @RequestParam BigDecimal coverageAmount,
                                HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        try {
            policyService.createTemplate(type, provider, premium, coverageAmount);
            return "redirect:/policies/admin/policies";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "admin-policies";
        }
    }

    @PostMapping("/purchase")
    public String purchasePolicy(@RequestParam Long templateId, @RequestParam LocalDate startDate,
                                @RequestParam LocalDate expiryDate, HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        try {
            Policy policy = policyService.purchasePolicy(userId, templateId, startDate, expiryDate);
            model.addAttribute("success", "Policy purchased successfully!");
            return "redirect:/policies/my-policies";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
