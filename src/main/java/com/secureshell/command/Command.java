package com.secureshell.command;

/**
 * Interface for all executable commands in the shell.
 */
public interface Command {
    /**
     * Executes the command with the given arguments.
     * 
     * @param args Command arguments
     * @return CommandResult containing execution status and output
     */
    CommandResult execute(String[] args);
    
    /**
     * Returns the name of the command.
     */
    String getName();
    
    /**
     * Returns a description of what the command does.
     */
    String getDescription();
}
