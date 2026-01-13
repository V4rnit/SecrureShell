package com.secureshell.config;

import com.secureshell.util.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration manager for SecureShell.
 * Loads and manages application settings.
 */
public class ShellConfig {
    private static final Logger logger = Logger.getInstance();
    private static final String CONFIG_FILE = "config.properties";
    
    private String jwtSecret;
    private long sessionTimeoutMinutes;
    private int maxThreads;
    private boolean enableLogging;
    private String logLevel;

    private ShellConfig() {
        loadDefaults();
    }

    public static ShellConfig load() {
        ShellConfig config = new ShellConfig();
        config.loadFromFile();
        return config;
    }

    private void loadDefaults() {
        this.jwtSecret = "SecureShellSecretKeyForJWTTokenGeneration2024";
        this.sessionTimeoutMinutes = 30;
        this.maxThreads = 4;
        this.enableLogging = true;
        this.logLevel = "INFO";
    }

    private void loadFromFile() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
            this.jwtSecret = props.getProperty("jwt.secret", jwtSecret);
            this.sessionTimeoutMinutes = Long.parseLong(
                props.getProperty("session.timeout.minutes", String.valueOf(sessionTimeoutMinutes)));
            this.maxThreads = Integer.parseInt(
                props.getProperty("threads.max", String.valueOf(maxThreads)));
            this.enableLogging = Boolean.parseBoolean(
                props.getProperty("logging.enabled", String.valueOf(enableLogging)));
            this.logLevel = props.getProperty("logging.level", logLevel);
            logger.info("Configuration loaded from file");
        } catch (IOException e) {
            logger.info("Configuration file not found, using defaults");
            saveDefaults();
        } catch (Exception e) {
            logger.warn("Error loading configuration: " + e.getMessage() + ", using defaults");
        }
    }

    private void saveDefaults() {
        Properties props = new Properties();
        props.setProperty("jwt.secret", jwtSecret);
        props.setProperty("session.timeout.minutes", String.valueOf(sessionTimeoutMinutes));
        props.setProperty("threads.max", String.valueOf(maxThreads));
        props.setProperty("logging.enabled", String.valueOf(enableLogging));
        props.setProperty("logging.level", logLevel);

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            props.store(fos, "SecureShell Configuration");
            logger.info("Default configuration saved to " + CONFIG_FILE);
        } catch (IOException e) {
            logger.warn("Could not save configuration file: " + e.getMessage());
        }
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public long getSessionTimeoutMinutes() {
        return sessionTimeoutMinutes;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public boolean isLoggingEnabled() {
        return enableLogging;
    }

    public String getLogLevel() {
        return logLevel;
    }
}
