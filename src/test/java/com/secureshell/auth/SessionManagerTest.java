package com.secureshell.auth;

import com.secureshell.config.ShellConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {
    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        ShellConfig config = ShellConfig.load();
        sessionManager = new SessionManager(config);
    }

    @Test
    void testSuccessfulAuthentication() {
        assertTrue(sessionManager.authenticate("admin", "admin123"));
        assertTrue(sessionManager.isSessionValid());
    }

    @Test
    void testFailedAuthentication() {
        assertFalse(sessionManager.authenticate("admin", "wrongpassword"));
        assertFalse(sessionManager.isSessionValid());
    }

    @Test
    void testSessionInvalidation() {
        sessionManager.authenticate("user", "user123");
        assertTrue(sessionManager.isSessionValid());
        sessionManager.invalidateSession();
        assertFalse(sessionManager.isSessionValid());
    }
}
