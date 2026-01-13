package com.secureshell.command;

/**
 * Represents a single command node in a pipeline.
 */
public class CommandNode {
    private final String[] command;

    public CommandNode(String[] command) {
        this.command = command != null ? command.clone() : new String[0];
    }

    public String[] getCommand() {
        return command.clone();
    }

    public String getCommandName() {
        return command.length > 0 ? command[0] : "";
    }
}
