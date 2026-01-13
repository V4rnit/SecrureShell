package com.secureshell.commands;

import com.secureshell.auth.SessionManager;
import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;

public class WhoamiCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        String username = System.getProperty("user.name");
        return CommandResult.success(username);
    }

    @Override
    public String getName() {
        return "whoami";
    }

    @Override
    public String getDescription() {
        return "Display current user";
    }
}
