package com.secureshell.command;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {
    private final boolean success;
    private final String errorMessage;
    private final String output;

    public CommandResult(boolean success, String errorMessage, String output) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.output = output;
    }

    public static CommandResult success(String output) {
        return new CommandResult(true, null, output);
    }

    public static CommandResult failure(String errorMessage) {
        return new CommandResult(false, errorMessage, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getOutput() {
        return output;
    }
}
