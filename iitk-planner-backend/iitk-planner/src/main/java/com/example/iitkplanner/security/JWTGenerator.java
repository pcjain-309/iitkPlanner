package com.example.iitkplanner.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTGenerator {

    // The secret key for signing the JWT
    @Value("${jwt.secret}")
    private String secretKey;

    // The validity period of the JWT in milliseconds
    @Value("${jwt.expiration}")
    private long expirationTime;

    public String generateToken(String subject) {
        // Calculate the expiration date based on the current time and the validity period
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        // Generate the JWT with the specified subject, expiration date, and signing algorithm
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractSubject(String token) {
        // Extract the subject (email) from the JWT
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
