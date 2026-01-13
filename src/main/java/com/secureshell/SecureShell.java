package com.secureshell;

import com.secureshell.auth.SessionManager;
import com.secureshell.command.CommandExecutor;
import com.secureshell.command.CommandParser;
import com.secureshell.command.CommandPipeline;
import com.secureshell.config.ShellConfig;
import com.secureshell.io.FileManager;
import com.secureshell.util.Logger;

import java.util.Scanner;

/**
 * Main entry point for the SecureShell application.
 * Provides an interactive command-line interface with JWT-based authentication
 * and multithreaded command execution.
 */
public class SecureShell {
    private static final Logger logger = Logger.getInstance();
    private SessionManager sessionManager;
    private CommandExecutor commandExecutor;
    private CommandParser commandParser;
    private FileManager fileManager;
    private boolean running;

    public SecureShell() {
        ShellConfig config = ShellConfig.load();
        this.sessionManager = new SessionManager(config);
        this.commandExecutor = new CommandExecutor(config);
        this.commandParser = new CommandParser();
        this.fileManager = new FileManager();
        this.running = false;
    }

    public void start() {
        logger.info("SecureShell v1.0.0 starting...");
        running = true;

        try (Scanner scanner = new Scanner(System.in)) {
            printWelcomeMessage();

            // Initial authentication
            if (!authenticate(scanner)) {
                logger.error("Authentication failed. Exiting.");
                return;
            }

            logger.info("Session authenticated successfully");
            printHelp();

            // Main command loop
            while (running) {
                try {
                    System.out.print("secure-shell> ");
                    String input = scanner.nextLine().trim();

                    if (input.isEmpty()) {
                        continue;
                    }

                    if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                        shutdown();
                        break;
                    }

                    processCommand(input);

                } catch (Exception e) {
                    logger.error("Error processing command: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            logger.error("Fatal error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private boolean authenticate(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        return sessionManager.authenticate(username, password);
    }

    private void processCommand(String input) {
        // Check session validity before processing
        if (!sessionManager.isSessionValid()) {
            logger.warn("Session expired. Please re-authenticate.");
            return;
        }

        // Track command history
        com.secureshell.util.CommandHistory.getInstance().addCommand(input);

        // Parse command for pipelines
        if (input.contains("|")) {
            CommandPipeline pipeline = commandParser.parsePipeline(input);
            commandExecutor.executePipeline(pipeline);
        } else {
            String[] parts = commandParser.parseCommand(input);
            commandExecutor.executeCommand(parts, input);
        }
    }

    private void printWelcomeMessage() {
        System.out.println("========================================");
        System.out.println("   SecureShell - Secure Command Shell");
        System.out.println("   Version 1.0.0");
        System.out.println("========================================");
        System.out.println();
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  help              - Show this help message");
        System.out.println("  ls [path]         - List directory contents");
        System.out.println("  cat <file>        - Display file contents");
        System.out.println("  grep <pattern>    - Search for pattern in files");
        System.out.println("  echo <text>       - Echo text to output");
        System.out.println("  pwd               - Print working directory");
        System.out.println("  cd <path>         - Change directory");
        System.out.println("  mkdir <path>      - Create directory");
        System.out.println("  touch <file>      - Create empty file");
        System.out.println("  rm <path>         - Remove file or directory");
        System.out.println("  whoami            - Display current user");
        System.out.println("  history           - Show command history");
        System.out.println("  alias [name] [cmd] - Create or list aliases");
        System.out.println("  unalias <name>    - Remove an alias");
        System.out.println("  export [VAR=val]  - Set or display environment variables");
        System.out.println("  jobs              - List background jobs");
        System.out.println("  fg <job_id>       - Bring job to foreground");
        System.out.println("  source <file>     - Execute script file");
        System.out.println("  exit/quit         - Exit the shell");
        System.out.println();
        System.out.println("Pipeline support: command1 | command2 | command3");
        System.out.println("Output redirection: command > file  or  command >> file");
        System.out.println("Background jobs: command &");
        System.out.println();
    }

    private void shutdown() {
        logger.info("Shutting down SecureShell...");
        running = false;
        sessionManager.invalidateSession();
        commandExecutor.shutdown();
    }

    private void cleanup() {
        if (commandExecutor != null) {
            commandExecutor.shutdown();
        }
        if (sessionManager != null) {
            sessionManager.invalidateSession();
        }
        logger.info("Cleanup completed");
    }

    public static void main(String[] args) {
        SecureShell shell = new SecureShell();
        shell.start();
    }
}
