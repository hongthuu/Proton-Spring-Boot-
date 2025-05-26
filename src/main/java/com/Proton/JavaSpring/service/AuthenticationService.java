package com.Proton.JavaSpring.service;

import com.Proton.JavaSpring.dto.request.ResetPasswordDTO;
import com.Proton.JavaSpring.dto.request.SignInRequest;
import com.Proton.JavaSpring.dto.response.SignInResponse;
import com.Proton.JavaSpring.dto.response.TokenResponse;
import com.Proton.JavaSpring.entity.Token;
import com.Proton.JavaSpring.entity.User;
import com.Proton.JavaSpring.repository.UserRepository;
import com.Proton.JavaSpring.service.serviceImpl.JwtServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.Proton.JavaSpring.util.TokenType.*;
import static org.springframework.http.HttpHeaders.REFERER;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwtServiceImpl jwtServiceImpl;

    public SignInResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        String accessToken = jwtServiceImpl.generateToken(user);
        String refreshToken = jwtServiceImpl.generateRefreshToken(user);
        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(1L)
                .phoneNumber("phoneNumber")
                .role("ROLE_ADMIN")
                .build();
    }

    public TokenResponse refreshToken(HttpServletRequest request) {
        final String refreshToken = request.getHeader(REFERER);
        if (StringUtils.isBlank(refreshToken)) {
            throw new RuntimeException("Token must be not blank");
        }
        final String userName = jwtService.extractUserName(refreshToken, REFRESH_TOKEN);
        Optional<User> user = userRepository.findByUsername(userName);
        if (!jwtService.isTokenValid(refreshToken, REFRESH_TOKEN, user.get())) {
            throw new RuntimeException("Not allow access with this token");
        }

        String accessToken = jwtService.generateToken(user.get());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.get().getUserId())
                .build();
    }

    public String removeToken(HttpServletRequest request) {
        final String token = request.getHeader(REFERER);
        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("Token must be not blank");
        }

        String userName = jwtService.extractUserName(token, ACCESS_TOKEN);

        tokenService.delete(userName);

        return "Removed!";
    }


    public String forgotPassword(String email) {

        User user = userService.getUserByEmail(email);

        String resetToken = jwtService.generateResetToken(user);

        tokenService.save(Token.builder()
                .username(user.getUsername())
                .resetToken(resetToken).build());

        String confirmLink = String.format("curl --location 'http://localhost:80/auth/reset-password' \\\n" +
                "--header 'accept: */*' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--data '%s'", resetToken);

        return resetToken;
    }

    public String resetPassword(String secretKey) {
        var user = validateToken(secretKey);
        tokenService.getByUsername(user.getUsername());

        return "Reset";
    }

    public String changePassword(ResetPasswordDTO request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        var user = validateToken(request.getSecretKey());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.saveUser(user);

        return "Changed";
    }

    private User validateToken(String token) {
        var userName = jwtService.extractUserName(token, RESET_TOKEN);
        var user = userService.getByUsername(userName);
        if (!user.isEnabled()) {
            throw new RuntimeException("User not active");
        }

        return user;
    }
}
