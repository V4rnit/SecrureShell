package com.secureshell.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages command history for the shell.
 */
public class CommandHistory {
    private static CommandHistory instance;
    private final List<String> history;
    private static final int MAX_HISTORY_SIZE = 100;

    private CommandHistory() {
        this.history = new ArrayList<>();
    }

    public static CommandHistory getInstance() {
        if (instance == null) {
            instance = new CommandHistory();
        }
        return instance;
    }

    public void addCommand(String command) {
        if (command != null && !command.trim().isEmpty()) {
            history.add(command);
            if (history.size() > MAX_HISTORY_SIZE) {
                history.remove(0);
            }
        }
    }

    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    public void clear() {
        history.clear();
    }
}
