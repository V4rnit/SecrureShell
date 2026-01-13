package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;

import java.io.File;
import java.io.IOException;

public class TouchCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        if (args.length == 0) {
            return CommandResult.failure("Usage: touch <file>");
        }

        String filePath = args[0];
        try {
            File file = new File(filePath);
            if (file.exists()) {
                // Update timestamp
                file.setLastModified(System.currentTimeMillis());
                return CommandResult.success("File timestamp updated: " + filePath);
            } else {
                // Create new file
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                boolean created = file.createNewFile();
                if (created) {
                    return CommandResult.success("File created: " + filePath);
                } else {
                    return CommandResult.failure("Failed to create file: " + filePath);
                }
            }
        } catch (IOException e) {
            return CommandResult.failure("Error creating file: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "touch";
    }

    @Override
    public String getDescription() {
        return "Create empty file or update timestamp";
    }
}
