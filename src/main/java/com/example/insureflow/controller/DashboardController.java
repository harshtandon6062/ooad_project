package com.example.insureflow.controller;

import com.example.insureflow.model.User;
import com.example.insureflow.service.DashboardService;
import com.example.insureflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String dashboard(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()).get();
        Map<String, Object> data = dashboardService.getUserDashboard(user.getId());
        model.addAllAttributes(data);
        model.addAttribute("user", user);
        return "dashboard";
    }
}