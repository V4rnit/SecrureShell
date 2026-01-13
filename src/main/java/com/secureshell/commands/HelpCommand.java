package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;
import com.secureshell.command.CommandRegistry;

public class HelpCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        CommandRegistry registry = new CommandRegistry();
        StringBuilder output = new StringBuilder();
        output.append("Available commands:\n\n");

        registry.getAllCommands().values().forEach(cmd -> {
            output.append(String.format("  %-15s - %s\n", cmd.getName(), cmd.getDescription()));
        });

        output.append("\nPipeline support: command1 | command2 | command3\n");
        output.append("Use 'exit' or 'quit' to exit the shell\n");

        return CommandResult.success(output.toString());
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Show help message";
    }
}
