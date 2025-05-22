package com.Proton.JavaSpring.service;

import com.Proton.JavaSpring.dto.request.SignInRequest;
import com.Proton.JavaSpring.dto.response.SignInResponse;
import com.Proton.JavaSpring.repository.UserRepository;
import com.Proton.JavaSpring.service.serviceImpl.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtServiceImpl jwtServiceImpl;

    public SignInResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var accessToken = jwtServiceImpl.generateToken(user);
        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken("refresh_token")
                .userId(1L)
                .phoneNumber("phoneNumber")
                .role("ROLE_ADMIN")
                .build();
    }
}
