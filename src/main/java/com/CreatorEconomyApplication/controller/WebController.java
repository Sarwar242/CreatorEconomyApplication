package com.CreatorEconomyApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web Controller for serving static HTML pages
 */
@Controller
public class WebController {
    
    /**
     * Serve the main dashboard page
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/dashboard.html";
    }
    
    /**
     * Serve the analytics page
     */
    @GetMapping("/analytics")
    public String analytics() {
        return "redirect:/analytics.html";
    }
  
    
    @GetMapping("/login")
    public String login() {
        return "redirect:/login.html";
    } 

    @GetMapping("/register")
    public String register() {
        return "redirect:/register.html";
    }   

    /**
     * Serve the home page (redirects to dashboard)
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard.html";
    }   
} 