package com.secureshell.command;

/**
 * Parses output redirection operators (> and >>) from command input.
 */
public class RedirectionParser {
    public static class RedirectionInfo {
        private final String command;
        private final String outputFile;
        private final boolean append;

        public RedirectionInfo(String command, String outputFile, boolean append) {
            this.command = command;
            this.outputFile = outputFile;
            this.append = append;
        }

        public String getCommand() { return command; }
        public String getOutputFile() { return outputFile; }
        public boolean isAppend() { return append; }
        public boolean hasRedirection() { return outputFile != null; }
    }

    public static RedirectionInfo parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new RedirectionInfo(input, null, false);
        }

        // Check for >> (append)
        int appendIndex = input.indexOf(">>");
        if (appendIndex != -1) {
            String command = input.substring(0, appendIndex).trim();
            String outputFile = input.substring(appendIndex + 2).trim();
            return new RedirectionInfo(command, outputFile, true);
        }

        // Check for > (overwrite)
        int overwriteIndex = input.indexOf(">");
        if (overwriteIndex != -1) {
            String command = input.substring(0, overwriteIndex).trim();
            String outputFile = input.substring(overwriteIndex + 1).trim();
            return new RedirectionInfo(command, outputFile, false);
        }

        return new RedirectionInfo(input, null, false);
    }
}
