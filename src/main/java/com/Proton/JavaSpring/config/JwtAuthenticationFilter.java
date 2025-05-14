package com.Proton.JavaSpring.config;

import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.crypto.MACVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String SIGNER_KEY;

    public JwtAuthenticationFilter(String signerKey) {
        this.SIGNER_KEY = signerKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            log.info("Processing request: {}", request.getRequestURI());

            // Check if this is an allowed endpoint, bypass filter
            String requestURI = request.getRequestURI();
            if (requestURI.equals("/token") ||
                    requestURI.equals("/login") ||
                    requestURI.equals("/register")) {
                log.info("Public endpoint detected, bypassing JWT check");
                filterChain.doFilter(request, response);
                return;
            }

            String authHeader = request.getHeader("Authorization");
            log.info("Authorization header: {}", (authHeader != null) ? "Present" : "Not present");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("No valid Authorization header found");
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            try {
                SignedJWT signedJWT = SignedJWT.parse(token);

                // Log the claims for debugging
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                log.info("JWT Claims: {}", claims.getClaims());

                boolean verified = signedJWT.verify(new MACVerifier(SIGNER_KEY.getBytes()));
                Date expiry = claims.getExpirationTime();
                Date now = new Date();

                log.info("Token verification: {}, Expired: {}", verified, expiry != null && expiry.before(now));

                if (verified && (expiry == null || expiry.after(now))) {
                    // Try different claim names that might be used for account ID
                    Long accountId = null;

                    if (claims.getClaims().containsKey("account_id")) {
                        accountId = claims.getLongClaim("account_id");
                    } else if (claims.getClaims().containsKey("accountId")) {
                        accountId = claims.getLongClaim("accountId");
                    } else if (claims.getClaims().containsKey("sub")) {
                        // Try parsing subject as account ID
                        try {
                            accountId = Long.parseLong(claims.getSubject());
                        } catch (NumberFormatException e) {
                            log.warn("Subject claim exists but is not a valid Long: {}", claims.getSubject());
                        }
                    }

                    log.info("Extracted accountId from JWT: {}", accountId);

                    if (accountId != null) {
                        // Set accountId into request attribute
                        request.setAttribute("account_id", accountId);

                        // Create authentication object and set in SecurityContextHolder
                        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_USER"));

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        accountId, null, authorities);

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        log.warn("Could not extract accountId from JWT claims");
                    }
                } else {
                    log.warn("Token validation failed: verified={}, expired={}",
                            verified,
                            expiry != null && expiry.before(now));
                    SecurityContextHolder.clearContext();
                }
            } catch (Exception e) {
                log.error("Error processing JWT token: {}", e.getMessage(), e);
                SecurityContextHolder.clearContext();
            }
        } catch (Exception e) {
            log.error("Unexpected error in JWT filter: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}