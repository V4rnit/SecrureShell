package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;
import com.secureshell.util.AliasManager;

public class UnaliasCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        if (args.length == 0) {
            return CommandResult.failure("Usage: unalias <alias>");
        }

        AliasManager aliasManager = AliasManager.getInstance();
        String alias = args[0];

        if (aliasManager.hasAlias(alias)) {
            aliasManager.unsetAlias(alias);
            return CommandResult.success("Alias removed: " + alias);
        } else {
            return CommandResult.failure("Alias not found: " + alias);
        }
    }

    @Override
    public String getName() {
        return "unalias";
    }

    @Override
    public String getDescription() {
        return "Remove a command alias";
    }
}
