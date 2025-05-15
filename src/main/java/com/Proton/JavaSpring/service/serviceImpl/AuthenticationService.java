package com.Proton.JavaSpring.service.serviceImpl;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@Data
public class AuthenticationService {

    @Value("${jwt.signer.key:eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwcm90b24iLCJzdWIiOiIxMjMiLCJhY2NvdW50X2lkIjoxMjMsImV4cCI6MTc0NzEzMTI1OSwiaWF0IjoxNzQ3MTI3NjU5LCJqdGkiOiJhOWQyMWE1YS00NTNlLTQ4YzMtODE4ZC0yOGIzZjdhMjBmMDEifQ.yQTsqpH6m_jEiPRI-D9ubittSb4-EEfickKjsRoTSdE}")
    private String SIGNER_KEY;

    @Value("${jwt.expiry.minutes:60}")
    private long expiryMinutes;

    public String generateToken(Long accountId) {
        try {
            JWSSigner signer = new MACSigner(SIGNER_KEY.getBytes());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(accountId.toString()) // Setting subject as account ID string
                    .issuer("proton")
                    .jwtID(UUID.randomUUID().toString())
                    .claim("account_id", accountId) // Using "account_id" as the claim name
                    .expirationTime(Date.from(Instant.now().plusSeconds(expiryMinutes * 60)))
                    .issueTime(new Date())
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.HS256).build(),
                    claimsSet);

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }
}