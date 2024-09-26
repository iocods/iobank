package iocode.web.app.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * This class provides JWT (JSON Web Tokens) related services.
 * It generates, verifies, and extracts information from JWT tokens.
 */
@Service
public class JwtService {

    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    /**
     * The secret key used to sign and verify JWT tokens.
     * This value is loaded from the application properties file.
     */
    @Value("${jwtSecret}")
    private String jwtSecret;

    /**
     * Generates a secret key from the jwtSecret.
     *
     * @return The generated secret key.
     */
    public SecretKey generateKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token for the given username.
     *
     * @param username The username for which the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(generateKey())
                .compact();
    }

    /**
     * Extracts the claims from the given JWT token.
     *
     * @param token The JWT token from which claims are extracted.
     * @return The extracted claims.
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts the subject from the given JWT token.
     *
     * @param token The JWT token from which the subject is extracted.
     * @return The extracted subject.
     */
    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Checks if the given JWT token is valid.
     *
     * @param token The JWT token to be validated.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token) {
        return new Date().before(extractExpiration(token));
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token The JWT token from which the expiration date is extracted.
     * @return The extracted expiration date.
     */
    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }
}