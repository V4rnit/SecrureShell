package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class GrepCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        if (args.length == 0) {
            return CommandResult.failure("Usage: grep <pattern> [file]");
        }

        String pattern = args[0];
        List<String> filesToSearch = new ArrayList<>();

        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                filesToSearch.add(args[i]);
            }
        }

        try {
            Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            List<String> matches = new ArrayList<>();

            if (filesToSearch.isEmpty()) {
                // Search in piped input (would be in args if from pipeline)
                return CommandResult.failure("No input provided for grep");
            }

            for (String filePath : filesToSearch) {
                Path path = Paths.get(filePath);
                if (Files.exists(path) && Files.isRegularFile(path)) {
                    List<String> lines = Files.readAllLines(path);
                    for (int i = 0; i < lines.size(); i++) {
                        if (compiledPattern.matcher(lines.get(i)).find()) {
                            matches.add(filePath + ":" + (i + 1) + ":" + lines.get(i));
                        }
                    }
                }
            }

            if (matches.isEmpty()) {
                return CommandResult.success("No matches found");
            }

            return CommandResult.success(String.join("\n", matches));
        } catch (PatternSyntaxException e) {
            return CommandResult.failure("Invalid pattern: " + e.getMessage());
        } catch (IOException e) {
            return CommandResult.failure("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "grep";
    }

    @Override
    public String getDescription() {
        return "Search for pattern in files";
    }
}
