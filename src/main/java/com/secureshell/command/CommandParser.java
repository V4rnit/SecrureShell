package com.secureshell.command;

import com.secureshell.util.AliasManager;
import com.secureshell.util.EnvironmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Parses command input strings into executable command structures.
 * Handles both single commands and pipeline parsing, aliases, and environment variables.
 */
public class CommandParser {
    private static final Pattern PIPELINE_PATTERN = Pattern.compile("\\|");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private final AliasManager aliasManager;
    private final EnvironmentManager environmentManager;

    public CommandParser() {
        this.aliasManager = AliasManager.getInstance();
        this.environmentManager = EnvironmentManager.getInstance();
    }

    /**
     * Parses a command string into an array of command parts.
     * Expands aliases and environment variables.
     */
    public String[] parseCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new String[0];
        }

        // Expand environment variables
        String expanded = environmentManager.expandVariables(input);
        
        // Expand aliases
        expanded = aliasManager.expandAlias(expanded);

        return WHITESPACE_PATTERN.split(expanded.trim());
    }

    /**
     * Parses a pipeline string into a CommandPipeline object.
     */
    public CommandPipeline parsePipeline(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new CommandPipeline();
        }

        String[] pipelineParts = PIPELINE_PATTERN.split(input);
        List<CommandNode> nodes = new ArrayList<>();

        for (String part : pipelineParts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                String[] commandParts = parseCommand(trimmed);
                if (commandParts.length > 0) {
                    nodes.add(new CommandNode(commandParts));
                }
            }
        }

        return new CommandPipeline(nodes);
    }

    /**
     * Checks if the input string contains a pipeline.
     */
    public boolean isPipeline(String input) {
        return input != null && input.contains("|");
    }
}
