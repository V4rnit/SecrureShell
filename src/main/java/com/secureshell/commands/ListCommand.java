package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;
import com.secureshell.io.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListCommand implements Command {
    private final FileManager fileManager;

    public ListCommand() {
        this.fileManager = new FileManager();
    }

    @Override
    public CommandResult execute(String[] args) {
        String path = args.length > 0 ? args[0] : ".";
        
        try {
            File directory = new File(path);
            if (!directory.exists()) {
                return CommandResult.failure("Directory does not exist: " + path);
            }
            
            if (!directory.isDirectory()) {
                return CommandResult.failure("Not a directory: " + path);
            }

            File[] files = directory.listFiles();
            if (files == null) {
                return CommandResult.failure("Cannot read directory: " + path);
            }

            List<String> output = new ArrayList<>();
            for (File file : files) {
                String name = file.getName();
                if (file.isDirectory()) {
                    output.add(name + "/");
                } else {
                    output.add(name);
                }
            }

            return CommandResult.success(String.join("\n", output));
        } catch (Exception e) {
            return CommandResult.failure("Error listing directory: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "ls";
    }

    @Override
    public String getDescription() {
        return "List directory contents";
    }
}
