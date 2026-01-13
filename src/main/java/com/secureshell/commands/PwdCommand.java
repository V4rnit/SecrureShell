package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;

import java.nio.file.Paths;

public class PwdCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        String currentDir = System.getProperty("user.dir");
        return CommandResult.success(currentDir);
    }

    @Override
    public String getName() {
        return "pwd";
    }

    @Override
    public String getDescription() {
        return "Print working directory";
    }
}
