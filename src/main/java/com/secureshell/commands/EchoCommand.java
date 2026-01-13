package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;

public class EchoCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        if (args.length == 0) {
            return CommandResult.success("");
        }

        String output = String.join(" ", args);
        return CommandResult.success(output);
    }

    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getDescription() {
        return "Echo text to output";
    }
}
