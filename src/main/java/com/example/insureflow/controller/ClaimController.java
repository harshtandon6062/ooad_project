package com.example.insureflow.controller;

import com.example.insureflow.model.Claim;
import com.example.insureflow.model.ClaimStatus;
import com.example.insureflow.model.User;
import com.example.insureflow.service.ClaimService;
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

    @GetMapping
    public String myClaims(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        List<Claim> claims = claimService.getUserClaims(userId);
        User user = (User) session.getAttribute("user");
        model.addAttribute("claims", claims);
        model.addAttribute("user", user);
        return "claims";
    }

    @GetMapping("/submit")
    public String submitClaimForm(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "claim-submit";
    }

    @PostMapping("/submit")
    public String submitClaim(@RequestParam Long policyId, @RequestParam BigDecimal amount,
                             @RequestParam String description, HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        try {
            claimService.submitClaim(policyId, userId, amount, description);
            return "redirect:/claims";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
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
