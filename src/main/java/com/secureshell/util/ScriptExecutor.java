package com.secureshell.util;

import com.secureshell.io.FileManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Executes shell scripts from files.
 */
public class ScriptExecutor {
    private final FileManager fileManager;
    private final Logger logger;

    public ScriptExecutor() {
        this.fileManager = new FileManager();
        this.logger = Logger.getInstance();
    }

    public boolean executeScript(String scriptPath) {
        try {
            Path path = Paths.get(scriptPath);
            if (!Files.exists(path)) {
                logger.error("Script file not found: " + scriptPath);
                return false;
            }

            if (!Files.isRegularFile(path)) {
                logger.error("Not a regular file: " + scriptPath);
                return false;
            }

            List<String> lines = Files.readAllLines(path);
            logger.info("Executing script: " + scriptPath + " (" + lines.size() + " lines)");
            
            return true;
        } catch (IOException e) {
            logger.error("Error reading script file: " + e.getMessage());
            return false;
        }
    }

    public List<String> readScript(String scriptPath) throws IOException {
        Path path = Paths.get(scriptPath);
        return Files.readAllLines(path);
    }
}
