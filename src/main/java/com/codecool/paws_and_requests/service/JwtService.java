package com.codecool.paws_and_requests.service;

import com.codecool.paws_and_requests.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    public static final int SECONDS = 60;
    private final SecretKey key;
    private final long expirationMinutes;

    public JwtService(final JwtProperties properties) {
        this.key = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(properties.secret())
        );
        this.expirationMinutes = properties.expirationMinutes();
    }

    public final String generateToken(final UserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(expirationMinutes * SECONDS);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }

    public final String extractUsername(final String token) {
        return parseClaims(token).getSubject();
    }

    public final boolean isValid(
            final String token,
            final UserDetails userDetails
    ) {
        try {
            var claims = parseClaims(token);
            return claims.getSubject().equals(userDetails.getUsername())
                    && claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims parseClaims(final String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public final long getExpirationMinutes() {
        return expirationMinutes;
    }
}
