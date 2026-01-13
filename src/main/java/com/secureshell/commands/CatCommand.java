package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;
import com.secureshell.io.FileManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CatCommand implements Command {
    private final FileManager fileManager;

    public CatCommand() {
        this.fileManager = new FileManager();
    }

    @Override
    public CommandResult execute(String[] args) {
        if (args.length == 0) {
            return CommandResult.failure("Usage: cat <file>");
        }

        String filePath = args[0];
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return CommandResult.failure("File does not exist: " + filePath);
            }

            if (Files.isDirectory(path)) {
                return CommandResult.failure("Cannot cat a directory: " + filePath);
            }

            String content = Files.readString(path);
            return CommandResult.success(content);
        } catch (IOException e) {
            return CommandResult.failure("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "cat";
    }

    @Override
    public String getDescription() {
        return "Display file contents";
    }
}
