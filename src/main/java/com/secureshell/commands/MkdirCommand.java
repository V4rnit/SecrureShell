package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;

import java.io.File;

public class MkdirCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        if (args.length == 0) {
            return CommandResult.failure("Usage: mkdir <directory>");
        }

        String path = args[0];
        try {
            File directory = new File(path);
            if (directory.exists()) {
                return CommandResult.failure("Directory already exists: " + path);
            }

            boolean created = directory.mkdirs();
            if (created) {
                return CommandResult.success("Directory created: " + path);
            } else {
                return CommandResult.failure("Failed to create directory: " + path);
            }
        } catch (Exception e) {
            return CommandResult.failure("Error creating directory: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "mkdir";
    }

    @Override
    public String getDescription() {
        return "Create directory";
    }
}
