package com.example.insureflow.controller;

import com.example.insureflow.model.User;
import com.example.insureflow.service.DashboardService;
import com.example.insureflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@Controller
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        Optional<User> user = userService.findById(userId);

        if (user.isPresent()) {
            Map<String, Object> dashboardData = dashboardService.getUserDashboard(userId);
            model.addAllAttributes(dashboardData);
            model.addAttribute("user", user.get());
            return "dashboard";
        }

        return "redirect:/login";
    }
}
