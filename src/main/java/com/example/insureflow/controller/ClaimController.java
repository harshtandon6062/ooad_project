package com.example.insureflow.controller;

import com.example.insureflow.model.Claim;
import com.example.insureflow.model.ClaimStatus;
import com.example.insureflow.model.User;
import com.example.insureflow.service.ClaimService;
import com.example.insureflow.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/claims")
public class ClaimController {
    @Autowired
    private ClaimService claimService;

    @Autowired
    private PolicyService policyService;

    @GetMapping
    public String myClaims(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        try {
            Long userId = (Long) session.getAttribute("userId");
            List<Claim> claims = claimService.getUserClaims(userId);
            User user = (User) session.getAttribute("user");
            model.addAttribute("claims", claims);
            model.addAttribute("user", user);
            return "claims";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage() != null ? e.getMessage() : "Unable to load your claims. Please try again later.");
            model.addAttribute("user", session.getAttribute("user"));
            return "error";
        }
    }

    @GetMapping("/submit")
    public String submitClaimForm(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("user");
        Long userId = (Long) session.getAttribute("userId");
        model.addAttribute("user", user);
        model.addAttribute("policies", policyService.getUserPolicies(userId));
        return "claim-submit";
    }

    @PostMapping("/submit")
    public String submitClaim(@RequestParam String policyId,
                              @RequestParam String amount,
                              @RequestParam String description,
                              HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        try {
            Long parsedPolicyId;
            BigDecimal parsedAmount;
            try {
                parsedPolicyId = Long.parseLong(policyId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Please select a valid policy.");
            }
            try {
                parsedAmount = new BigDecimal(amount);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Please enter a valid claim amount.");
            }

            claimService.submitClaim(parsedPolicyId, userId, parsedAmount, description);
            return "redirect:/claims";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage() != null ? e.getMessage() : "Unable to submit claim. Please check your inputs and try again.");
            model.addAttribute("policies", policyService.getUserPolicies(userId));
            model.addAttribute("policyId", policyId);
            model.addAttribute("amount", amount);
            model.addAttribute("description", description);
            return "claim-submit";
        }
    }

    @GetMapping("/{id}")
    public String getClaimDetails(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Optional<Claim> claim = claimService.getClaimById(id);
        if (claim.isPresent()) {
            User user = (User) session.getAttribute("user");
            model.addAttribute("claim", claim.get());
            model.addAttribute("user", user);
            return "claim-detail";
        }
        return "error";
    }

    @GetMapping("/adjuster/claims")
    public String reviewClaims(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("user");
        if (!user.getRole().name().equals("ADMIN") && !user.getRole().name().equals("CLAIMS_ADJUSTER")) {
            return "redirect:/dashboard";
        }

        List<Claim> claims = claimService.getAllClaims();
        model.addAttribute("claims", claims);
        model.addAttribute("user", user);
        return "claim-review";
    }

    @PostMapping("/{id}/approve")
    public String approveClaim(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        claimService.updateClaimStatus(id, ClaimStatus.APPROVED);
        return "redirect:/claims/adjuster/claims";
    }

    @PostMapping("/{id}/reject")
    public String rejectClaim(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        claimService.updateClaimStatus(id, ClaimStatus.REJECTED);
        return "redirect:/claims/adjuster/claims";
    }
}
