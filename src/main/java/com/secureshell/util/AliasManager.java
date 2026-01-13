package com.secureshell.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages command aliases for the shell.
 */
public class AliasManager {
    private static AliasManager instance;
    private final Map<String, String> aliases;

    private AliasManager() {
        this.aliases = new HashMap<>();
        initializeDefaultAliases();
    }

    public static AliasManager getInstance() {
        if (instance == null) {
            instance = new AliasManager();
        }
        return instance;
    }

    private void initializeDefaultAliases() {
        aliases.put("ll", "ls -la");
        aliases.put("la", "ls -a");
        aliases.put("..", "cd ..");
        aliases.put("...", "cd ../..");
    }

    public void setAlias(String alias, String command) {
        if (alias != null && command != null) {
            aliases.put(alias, command);
        }
    }

    public void unsetAlias(String alias) {
        aliases.remove(alias);
    }

    public String expandAlias(String input) {
        String[] parts = input.trim().split("\\s+", 2);
        String firstWord = parts[0];
        
        if (aliases.containsKey(firstWord)) {
            String expanded = aliases.get(firstWord);
            if (parts.length > 1) {
                return expanded + " " + parts[1];
            }
            return expanded;
        }
        
        return input;
    }

    public Map<String, String> getAllAliases() {
        return new HashMap<>(aliases);
    }

    public boolean hasAlias(String alias) {
        return aliases.containsKey(alias);
    }
}
