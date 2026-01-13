package com.secureshell.util;

/**
 * Simple logging utility for SecureShell.
 */
public class Logger {
    private static Logger instance;
    private final boolean enabled;

    private Logger() {
        this.enabled = true;
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void info(String message) {
        if (enabled) {
            System.out.println("[INFO] " + message);
        }
    }

    public void warn(String message) {
        if (enabled) {
            System.err.println("[WARN] " + message);
        }
    }

    public void error(String message) {
        if (enabled) {
            System.err.println("[ERROR] " + message);
        }
    }

    public void debug(String message) {
        if (enabled) {
            System.out.println("[DEBUG] " + message);
        }
    }
}
