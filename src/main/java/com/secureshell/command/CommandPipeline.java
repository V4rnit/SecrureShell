package com.secureshell.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pipeline of commands to be executed sequentially.
 * Each command's output becomes the input for the next command.
 */
public class CommandPipeline {
    private final List<CommandNode> nodes;

    public CommandPipeline() {
        this.nodes = new ArrayList<>();
    }

    public CommandPipeline(List<CommandNode> nodes) {
        this.nodes = nodes != null ? new ArrayList<>(nodes) : new ArrayList<>();
    }

    public List<CommandNode> getNodes() {
        return new ArrayList<>(nodes);
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public int size() {
        return nodes.size();
    }

    public void addNode(CommandNode node) {
        if (node != null) {
            nodes.add(node);
        }
    }
}
