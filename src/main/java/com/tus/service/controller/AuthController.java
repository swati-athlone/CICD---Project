package com.tus.service.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestParam String clientId, @RequestParam String clientSecret) {
        if ("admin".equals(clientId) && "password".equals(clientSecret)) {
            // Decode Base64 key correctly (ensuring it's 256 bits)
            byte[] decodedKey = Base64.getDecoder().decode(secretKey);
            SecretKey key = Keys.hmacShaKeyFor(decodedKey);

            String token = Jwts.builder()
                    .setSubject(clientId)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            return ResponseEntity.ok(Collections.singletonMap("token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }
}