package com.secureshell.commands;

import com.secureshell.command.Command;
import com.secureshell.command.CommandResult;
import com.secureshell.util.EnvironmentManager;

public class ExportCommand implements Command {
    @Override
    public CommandResult execute(String[] args) {
        if (args.length == 0) {
            // List all environment variables
            EnvironmentManager envManager = EnvironmentManager.getInstance();
            var variables = envManager.getAllVariables();
            
            StringBuilder output = new StringBuilder();
            for (var entry : variables.entrySet()) {
                output.append("export ").append(entry.getKey())
                      .append("=").append(entry.getValue()).append("\n");
            }
            return CommandResult.success(output.toString().trim());
        }

        if (args.length >= 1) {
            String arg = args[0];
            if (arg.contains("=")) {
                // Set variable: export VAR=value
                String[] parts = arg.split("=", 2);
                String name = parts[0];
                String value = parts.length > 1 ? parts[1] : "";
                
                EnvironmentManager envManager = EnvironmentManager.getInstance();
                envManager.setVariable(name, value);
                return CommandResult.success("Exported: " + name + "=" + value);
            } else {
                // Show variable value
                EnvironmentManager envManager = EnvironmentManager.getInstance();
                String value = envManager.getVariable(arg);
                if (value != null) {
                    return CommandResult.success(arg + "=" + value);
                } else {
                    return CommandResult.failure("Variable not found: " + arg);
                }
            }
        }

        return CommandResult.failure("Usage: export [VAR=value]");
    }

    @Override
    public String getName() {
        return "export";
    }

    @Override
    public String getDescription() {
        return "Set or display environment variables";
    }
}
