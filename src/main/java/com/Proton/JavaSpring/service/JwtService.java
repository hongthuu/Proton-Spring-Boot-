package com.Proton.JavaSpring.service;

import com.Proton.JavaSpring.util.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token, TokenType tokenType);

    String generateToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String generateResetToken(UserDetails user);

    boolean isTokenValid(String token, TokenType tokenType, UserDetails userDetails);
}