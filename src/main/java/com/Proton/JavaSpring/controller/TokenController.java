package com.Proton.JavaSpring.controller;

import com.Proton.JavaSpring.service.serviceImpl.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class TokenController {

    @Autowired
    AuthenticationService authService;

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestParam("accountId") Long accountId) {
        String token = authService.generateToken(accountId);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/token")
    public ResponseEntity<?> getAccountInfo(HttpServletRequest request) {
        // Log all attributes in the request for debugging
        log.info("All request attributes:");
        java.util.Enumeration<String> attributeNames = request.getAttributeNames();
        while(attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            log.info("Attribute: {} = {}", name, request.getAttribute(name));
        }

        // Get accountId from request attribute set by the JWT filter
        Long accountId = (Long) request.getAttribute("account_id");
        log.info("accountId from request attribute: {}", accountId);

        // You can also get it from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication principal: {}", authentication != null ? authentication.getPrincipal() : "null");

        Map<String, Object> response = new HashMap<>();
        response.put("accountId", accountId);
        response.put("message", "Account ID from JWT: " + accountId);

        return ResponseEntity.ok(response);
    }
}