package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;
import com.secureshell.util.ScriptExecutor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SourceCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        if (args.length == 0) {
            return CommandResult.failure("Usage: source <script_file>");
        }

        String scriptPath = args[0];
        ScriptExecutor executor = new ScriptExecutor();

        try {
            Path path = Paths.get(scriptPath);
            if (!Files.exists(path)) {
                return CommandResult.failure("Script file not found: " + scriptPath);
            }

            List<String> lines = Files.readAllLines(path);
            int executed = 0;
            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    executed++;
                }
            }

            return CommandResult.success("Script executed: " + scriptPath + " (" + executed + " commands)");
        } catch (IOException e) {
            return CommandResult.failure("Error reading script: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "source";
    }

    @Override
    public String getDescription() {
        return "Execute commands from a script file";
    }
}
