package com.secureshell.command;

import com.secureshell.config.ShellConfig;
import com.secureshell.io.FileManager;
import com.secureshell.util.JobManager;
import com.secureshell.util.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Executes commands with multithreading support for improved performance.
 * Handles both single commands and command pipelines.
 */
public class CommandExecutor {
    private static final Logger logger = Logger.getInstance();
    private final ExecutorService executorService;
    private final CommandRegistry commandRegistry;
    private final int maxThreads;
    private final AtomicInteger activeCommands;

    public CommandExecutor(ShellConfig config) {
        this.maxThreads = config.getMaxThreads();
        this.executorService = Executors.newFixedThreadPool(maxThreads);
        this.commandRegistry = new CommandRegistry();
        this.activeCommands = new AtomicInteger(0);
        logger.info("CommandExecutor initialized with " + maxThreads + " threads");
    }

    /**
     * Executes a single command with support for redirection and background jobs.
     */
    public void executeCommand(String[] commandParts, String originalInput) {
        if (commandParts == null || commandParts.length == 0) {
            return;
        }

        // Check for background execution
        boolean background = false;
        if (commandParts.length > 0 && commandParts[commandParts.length - 1].equals("&")) {
            background = true;
            commandParts = java.util.Arrays.copyOf(commandParts, commandParts.length - 1);
        }

        // Check for output redirection
        String fullCommand = String.join(" ", commandParts);
        RedirectionParser.RedirectionInfo redirInfo = RedirectionParser.parse(fullCommand);
        
        String commandName;
        String[] args;
        
        if (redirInfo.hasRedirection()) {
            String[] redirectedParts = redirInfo.getCommand().split("\\s+");
            commandName = redirectedParts[0];
            args = redirectedParts.length > 1 ? 
                java.util.Arrays.copyOfRange(redirectedParts, 1, redirectedParts.length) : 
                new String[0];
        } else {
            commandName = commandParts[0];
            args = commandParts.length > 1 ? 
                java.util.Arrays.copyOfRange(commandParts, 1, commandParts.length) : 
                new String[0];
        }

        activeCommands.incrementAndGet();
        
        Future<CommandResult> future = executorService.submit(() -> {
            try {
                Command command = commandRegistry.getCommand(commandName);
                if (command != null) {
                    CommandResult result = command.execute(args);
                    
                    // Handle output redirection
                    if (redirInfo.hasRedirection() && result.isSuccess() && result.getOutput() != null) {
                        writeToFile(redirInfo.getOutputFile(), result.getOutput(), redirInfo.isAppend());
                    }
                    
                    return result;
                } else {
                    return new CommandResult(false, "Command not found: " + commandName, null);
                }
            } catch (Exception e) {
                logger.error("Error executing command: " + e.getMessage());
                return new CommandResult(false, "Error: " + e.getMessage(), null);
            } finally {
                activeCommands.decrementAndGet();
            }
        });

        if (background) {
            // Run in background
            int jobId = JobManager.getInstance().addJob(originalInput, future);
            System.out.println("[" + jobId + "] " + originalInput);
        } else {
            // Run in foreground
            try {
                CommandResult result = future.get(30, TimeUnit.SECONDS);
                if (!redirInfo.hasRedirection()) {
                    displayResult(result);
                } else if (result.isSuccess()) {
                    System.out.println("Output redirected to: " + redirInfo.getOutputFile());
                }
            } catch (TimeoutException e) {
                logger.error("Command execution timeout");
                System.out.println("Error: Command execution timed out");
            } catch (Exception e) {
                logger.error("Error waiting for command result: " + e.getMessage());
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Executes a single command (backward compatibility).
     */
    public void executeCommand(String[] commandParts) {
        executeCommand(commandParts, String.join(" ", commandParts));
    }

    private void writeToFile(String filePath, String content, boolean append) {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            
            if (append) {
                Files.writeString(path, content + "\n", 
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                Files.writeString(path, content);
            }
        } catch (IOException e) {
            logger.error("Error writing to file: " + e.getMessage());
            System.err.println("Error: Could not write to file: " + filePath);
        }
    }

    /**
     * Executes a command pipeline with parallel processing where possible.
     */
    public void executePipeline(CommandPipeline pipeline) {
        if (pipeline == null || pipeline.isEmpty()) {
            return;
        }

        activeCommands.incrementAndGet();
        
        executorService.submit(() -> {
            try {
                List<CommandResult> results = new ArrayList<>();
                String previousOutput = null;

                for (CommandNode node : pipeline.getNodes()) {
                    String[] commandParts = node.getCommand();
                    String commandName = commandParts[0];
                    String[] args = commandParts.length > 1 ? 
                        java.util.Arrays.copyOfRange(commandParts, 1, commandParts.length) : 
                        new String[0];

                    Command command = commandRegistry.getCommand(commandName);
                    if (command == null) {
                        results.add(new CommandResult(false, 
                            "Command not found: " + commandName, null));
                        break;
                    }

                    // Pass previous output as input for pipeline
                    if (previousOutput != null) {
                        args = combineArgs(args, previousOutput);
                    }

                    CommandResult result = command.execute(args);
                    results.add(result);
                    
                    if (!result.isSuccess()) {
                        break;
                    }

                    previousOutput = result.getOutput();
                }

                // Display final pipeline result
                if (!results.isEmpty()) {
                    CommandResult finalResult = results.get(results.size() - 1);
                    displayResult(finalResult);
                }
            } catch (Exception e) {
                logger.error("Error executing pipeline: " + e.getMessage());
                System.out.println("Error: " + e.getMessage());
            } finally {
                activeCommands.decrementAndGet();
            }
        });
    }

    private String[] combineArgs(String[] args, String input) {
        List<String> combined = new ArrayList<>();
        for (String arg : args) {
            combined.add(arg);
        }
        combined.add(input);
        return combined.toArray(new String[0]);
    }

    private void displayResult(CommandResult result) {
        if (result.isSuccess()) {
            if (result.getOutput() != null && !result.getOutput().isEmpty()) {
                System.out.println(result.getOutput());
            }
        } else {
            System.err.println(result.getErrorMessage());
        }
    }

    /**
     * Shuts down the executor service gracefully.
     */
    public void shutdown() {
        logger.info("Shutting down CommandExecutor...");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("CommandExecutor shutdown complete");
    }

    public int getActiveCommandCount() {
        return activeCommands.get();
    }
}
