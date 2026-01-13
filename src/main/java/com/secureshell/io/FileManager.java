package com.secureshell.io;

import com.secureshell.util.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages file I/O operations for the shell.
 * Provides safe file operations with error handling.
 */
public class FileManager {
    private static final Logger logger = Logger.getInstance();

    public FileManager() {
    }

    /**
     * Reads the contents of a file.
     */
    public String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("File does not exist: " + filePath);
        }
        return Files.readString(path);
    }

    /**
     * Writes content to a file.
     */
    public void writeFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
    }

    /**
     * Appends content to a file.
     */
    public void appendToFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * Lists files in a directory.
     */
    public List<String> listFiles(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            throw new IOException("Directory does not exist: " + directoryPath);
        }

        List<String> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                files.add(entry.getFileName().toString());
            }
        }
        return files;
    }

    /**
     * Checks if a path exists.
     */
    public boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    /**
     * Checks if a path is a directory.
     */
    public boolean isDirectory(String path) {
        return Files.isDirectory(Paths.get(path));
    }

    /**
     * Gets file size in bytes.
     */
    public long getFileSize(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.size(path);
    }

    /**
     * Deletes a file or directory recursively.
     */
    public void deleteRecursively(String path) throws IOException {
        Path filePath = Paths.get(path);
        if (Files.isDirectory(filePath)) {
            Files.walkFileTree(filePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            Files.delete(filePath);
        }
    }
}
