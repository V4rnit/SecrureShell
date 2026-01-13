package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;

import java.io.File;

public class CdCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        String path = args.length > 0 ? args[0] : System.getProperty("user.home");
        
        try {
            File directory = new File(path);
            if (!directory.exists()) {
                return CommandResult.failure("Directory does not exist: " + path);
            }
            
            if (!directory.isDirectory()) {
                return CommandResult.failure("Not a directory: " + path);
            }

            System.setProperty("user.dir", directory.getAbsolutePath());
            return CommandResult.success("");
        } catch (Exception e) {
            return CommandResult.failure("Error changing directory: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "cd";
    }

    @Override
    public String getDescription() {
        return "Change directory";
    }
}
