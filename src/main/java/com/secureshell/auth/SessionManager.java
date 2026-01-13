package com.secureshell.auth;

import com.secureshell.config.ShellConfig;
import com.secureshell.util.Logger;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages JWT-based session authentication and authorization.
 * Provides secure token generation, validation, and session tracking.
 */
public class SessionManager {
    private static final Logger logger = Logger.getInstance();
    private final SecretKey secretKey;
    private final long sessionTimeoutMinutes;
    private final Map<String, String> userCredentials;
    private String currentToken;
    private Instant tokenExpiry;
    private final Map<String, SessionInfo> activeSessions;

    public SessionManager(ShellConfig config) {
        String secret = config.getJwtSecret();
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.sessionTimeoutMinutes = config.getSessionTimeoutMinutes();
        this.userCredentials = initializeCredentials();
        this.activeSessions = new ConcurrentHashMap<>();
    }

    /**
     * Authenticates a user and generates a JWT token.
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            logger.warn("Authentication attempt with null credentials");
            return false;
        }

        String storedPassword = userCredentials.get(username);
        if (storedPassword != null && storedPassword.equals(password)) {
            currentToken = generateToken(username);
            tokenExpiry = Instant.now().plus(sessionTimeoutMinutes, ChronoUnit.MINUTES);
            
            SessionInfo sessionInfo = new SessionInfo(username, currentToken, tokenExpiry);
            activeSessions.put(currentToken, sessionInfo);
            
            logger.info("User authenticated: " + username);
            return true;
        }

        logger.warn("Authentication failed for user: " + username);
        return false;
    }

    /**
     * Validates the current session token.
     */
    public boolean isSessionValid() {
        if (currentToken == null || tokenExpiry == null) {
            return false;
        }

        if (Instant.now().isAfter(tokenExpiry)) {
            logger.warn("Session token expired");
            return false;
        }

        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(currentToken);
            
            return activeSessions.containsKey(currentToken);
        } catch (Exception e) {
            logger.warn("Invalid session token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the username from the current session token.
     */
    public String getCurrentUsername() {
        if (!isSessionValid() || currentToken == null) {
            return null;
        }

        try {
            return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(currentToken)
                .getBody()
                .getSubject();
        } catch (Exception e) {
            logger.error("Error extracting username from token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Invalidates the current session.
     */
    public void invalidateSession() {
        if (currentToken != null) {
            activeSessions.remove(currentToken);
            currentToken = null;
            tokenExpiry = null;
            logger.info("Session invalidated");
        }
    }

    /**
     * Generates a JWT token for the authenticated user.
     */
    private String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("issuedAt", Instant.now().toString());
        claims.put("sessionId", generateSessionId());

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(sessionTimeoutMinutes, ChronoUnit.MINUTES)))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    private String generateSessionId() {
        return "session_" + System.currentTimeMillis() + "_" + 
               (int)(Math.random() * 10000);
    }

    private Map<String, String> initializeCredentials() {
        Map<String, String> creds = new HashMap<>();
        // Default credentials for demo purposes
        creds.put("admin", "admin123");
        creds.put("user", "user123");
        creds.put("guest", "guest123");
        return creds;
    }

    /**
     * Session information holder.
     */
    private static class SessionInfo {
        private final String username;
        private final String token;
        private final Instant expiry;

        public SessionInfo(String username, String token, Instant expiry) {
            this.username = username;
            this.token = token;
            this.expiry = expiry;
        }

        public String getUsername() { return username; }
        public String getToken() { return token; }
        public Instant getExpiry() { return expiry; }
    }
}
