package com.secureshell.command;

import com.secureshell.commands.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for all available commands in the shell.
 * Provides extensible command registration and lookup.
 */
public class CommandRegistry {
    private final Map<String, Command> commands;

    public CommandRegistry() {
        this.commands = new HashMap<>();
        registerDefaultCommands();
    }

    private void registerDefaultCommands() {
        registerCommand(new ListCommand());
        registerCommand(new CatCommand());
        registerCommand(new GrepCommand());
        registerCommand(new EchoCommand());
        registerCommand(new PwdCommand());
        registerCommand(new CdCommand());
        registerCommand(new MkdirCommand());
        registerCommand(new TouchCommand());
        registerCommand(new RmCommand());
        registerCommand(new WhoamiCommand());
        registerCommand(new HistoryCommand());
        registerCommand(new HelpCommand());
        registerCommand(new AliasCommand());
        registerCommand(new UnaliasCommand());
        registerCommand(new ExportCommand());
        registerCommand(new JobsCommand());
        registerCommand(new FgCommand());
        registerCommand(new SourceCommand());
    }

    public void registerCommand(Command command) {
        if (command != null && command.getName() != null) {
            commands.put(command.getName(), command);
        }
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public Map<String, Command> getAllCommands() {
        return new HashMap<>(commands);
    }
}
