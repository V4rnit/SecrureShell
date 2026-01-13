package com.secureshell.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages environment variables for the shell.
 */
public class EnvironmentManager {
    private static EnvironmentManager instance;
    private final Map<String, String> variables;

    private EnvironmentManager() {
        this.variables = new HashMap<>();
        initializeSystemVariables();
    }

    public static EnvironmentManager getInstance() {
        if (instance == null) {
            instance = new EnvironmentManager();
        }
        return instance;
    }

    private void initializeSystemVariables() {
        variables.put("HOME", System.getProperty("user.home"));
        variables.put("USER", System.getProperty("user.name"));
        variables.put("PWD", System.getProperty("user.dir"));
        variables.put("SHELL", "SecureShell");
        variables.put("VERSION", "1.0.0");
    }

    public void setVariable(String name, String value) {
        if (name != null && value != null) {
            variables.put(name, value);
            if (name.equals("PWD")) {
                System.setProperty("user.dir", value);
            }
        }
    }

    public String getVariable(String name) {
        return variables.get(name);
    }

    public void unsetVariable(String name) {
        if (!isSystemVariable(name)) {
            variables.remove(name);
        }
    }

    private boolean isSystemVariable(String name) {
        return name.equals("HOME") || name.equals("USER") || 
               name.equals("SHELL") || name.equals("VERSION");
    }

    public Map<String, String> getAllVariables() {
        return new HashMap<>(variables);
    }

    public String expandVariables(String input) {
        String result = input;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String varName = "$" + entry.getKey();
            result = result.replace(varName, entry.getValue());
        }
        return result;
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }
}
