package com.example.springapplication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/greeting", "/actuator/health").permitAll()
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        .requestMatchers("/api/customers/**", "/api/orders/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder,
            @Value("${app.security.admin.username}") String adminUsername,
            @Value("${app.security.admin.password}") String adminPassword,
            @Value("${app.security.customer.username}") String customerUsername,
            @Value("${app.security.customer.password}") String customerPassword) {
        return new InMemoryUserDetailsManager(
                User.withUsername(adminUsername)
                        .password(passwordEncoder.encode(adminPassword))
                        .roles("ADMIN")
                        .build(),
                User.withUsername(customerUsername)
                        .password(passwordEncoder.encode(customerPassword))
                        .roles("CUSTOMER")
                        .build());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}