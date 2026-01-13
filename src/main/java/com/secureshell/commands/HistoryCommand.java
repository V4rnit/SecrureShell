package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;
import com.secureshell.util.CommandHistory;

import java.util.List;

public class HistoryCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        CommandHistory history = CommandHistory.getInstance();
        List<String> commands = history.getHistory();
        
        if (commands.isEmpty()) {
            return CommandResult.success("No command history");
        }

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < commands.size(); i++) {
            output.append((i + 1)).append("  ").append(commands.get(i)).append("\n");
        }

        return CommandResult.success(output.toString().trim());
    }

    @Override
    public String getName() {
        return "history";
    }

    @Override
    public String getDescription() {
        return "Show command history";
    }
}
