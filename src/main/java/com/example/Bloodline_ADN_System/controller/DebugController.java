package com.example.Bloodline_ADN_System.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/auth")
    public ResponseEntity<?> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> response = new HashMap<>();
        response.put("principal", auth.getPrincipal());
        response.put("authorities", auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        response.put("isAuthenticated", auth.isAuthenticated());
        response.put("name", auth.getName());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/database")
    public ResponseEntity<?> checkDatabase() {
        Map<String, Object> response = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            response.put("status", "SUCCESS");
            response.put("message", "Database connection successful");
            response.put("database", connection.getMetaData().getDatabaseProductName());
            response.put("version", connection.getMetaData().getDatabaseProductVersion());
            response.put("url", connection.getMetaData().getURL());
            response.put("username", connection.getMetaData().getUserName());
            
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            response.put("status", "ERROR");
            response.put("message", "Database connection failed");
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }
}