package com.secureshell.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {
    private CommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new CommandParser();
    }

    @Test
    void testParseSimpleCommand() {
        String[] result = parser.parseCommand("ls -la");
        assertEquals(2, result.length);
        assertEquals("ls", result[0]);
        assertEquals("-la", result[1]);
    }

    @Test
    void testParsePipeline() {
        CommandPipeline pipeline = parser.parsePipeline("ls | grep test | cat");
        assertFalse(pipeline.isEmpty());
        assertEquals(3, pipeline.size());
    }

    @Test
    void testIsPipeline() {
        assertTrue(parser.isPipeline("ls | grep test"));
        assertFalse(parser.isPipeline("ls -la"));
    }
}
