package com.CreatorEconomyApplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Allow public access to static resources and main pages
                .requestMatchers("/", "/dashboard", "/dashboard.html", "/analytics", "/analytics.html", 
                               "/login", "/login.html", "/register", "/register.html","/actuator/**",
                               "/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                // Allow public access to API endpoints for now (you can secure these later)
                .requestMatchers("/api/**").permitAll()
                // Require authentication for everything else
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Disable CSRF for API endpoints

        return http.build();
    }
} 