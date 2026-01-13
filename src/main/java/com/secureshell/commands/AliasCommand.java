package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;
import com.secureshell.util.AliasManager;

import java.util.Map;

public class AliasCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        AliasManager aliasManager = AliasManager.getInstance();

        if (args.length == 0) {
            // List all aliases
            Map<String, String> aliases = aliasManager.getAllAliases();
            if (aliases.isEmpty()) {
                return CommandResult.success("No aliases defined");
            }

            StringBuilder output = new StringBuilder();
            for (Map.Entry<String, String> entry : aliases.entrySet()) {
                output.append(entry.getKey()).append("='").append(entry.getValue()).append("'\n");
            }
            return CommandResult.success(output.toString().trim());
        }

        if (args.length == 1) {
            // Show specific alias
            String alias = args[0];
            if (aliasManager.hasAlias(alias)) {
                return CommandResult.success(alias + "='" + aliasManager.getAllAliases().get(alias) + "'");
            } else {
                return CommandResult.failure("Alias not found: " + alias);
            }
        }

        if (args.length >= 2) {
            // Set alias
            String alias = args[0];
            String command = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
            aliasManager.setAlias(alias, command);
            return CommandResult.success("Alias set: " + alias + "='" + command + "'");
        }

        return CommandResult.failure("Usage: alias [name] [command]");
    }

    @Override
    public String getName() {
        return "alias";
    }

    @Override
    public String getDescription() {
        return "Create or list command aliases";
    }
}
