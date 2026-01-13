package com.secureshell.commands;

import com.secureshell.command.CommandResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EchoCommandTest {
    @Test
    void testEchoCommand() {
        EchoCommand command = new EchoCommand();
        CommandResult result = command.execute(new String[]{"Hello", "World"});
        
        assertTrue(result.isSuccess());
        assertEquals("Hello World", result.getOutput());
    }

    @Test
    void testEchoEmpty() {
        EchoCommand command = new EchoCommand();
        CommandResult result = command.execute(new String[]{});
        
        assertTrue(result.isSuccess());
        assertEquals("", result.getOutput());
    }
}
