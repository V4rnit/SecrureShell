package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;

import java.io.File;

public class RmCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        if (args.length == 0) {
            return CommandResult.failure("Usage: rm <file|directory>");
        }

        String path = args[0];
        boolean recursive = args.length > 1 && args[1].equals("-r");

        try {
            File file = new File(path);
            if (!file.exists()) {
                return CommandResult.failure("File or directory does not exist: " + path);
            }

            boolean deleted;
            if (file.isDirectory()) {
                if (recursive) {
                    deleted = deleteDirectory(file);
                } else {
                    return CommandResult.failure("Cannot remove directory without -r flag: " + path);
                }
            } else {
                deleted = file.delete();
            }

            if (deleted) {
                return CommandResult.success("Removed: " + path);
            } else {
                return CommandResult.failure("Failed to remove: " + path);
            }
        } catch (Exception e) {
            return CommandResult.failure("Error removing file: " + e.getMessage());
        }
    }

    private boolean deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }

    @Override
    public String getName() {
        return "rm";
    }

    @Override
    public String getDescription() {
        return "Remove file or directory";
    }
}
