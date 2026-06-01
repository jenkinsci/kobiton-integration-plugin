package io.jenkins.plugins.kobiton.shared.logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PluginLoggerTest {

    private ByteArrayOutputStream outContent;
    private PrintStream printStream;

    @BeforeEach
    void setup() {
        outContent = new ByteArrayOutputStream();
        printStream = new PrintStream(outContent);
    }

    @Test
    void pluginLogger_Constructor_IsPrivate() throws NoSuchMethodException {
        Constructor<PluginLogger> constructor = PluginLogger.class.getDeclaredConstructor();
        boolean expected = true;

        assertEquals(expected, Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);

        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void log_MessageAndPrintStreamGiven_ShouldPrintLog() {
        String message = "Log message";

        PluginLogger.log(message, printStream);
        String output = outContent.toString().trim();

        assertEquals("Log message", output);
    }

    @Test
    void error_MessageLocationAndPrintStreamGiven_ShouldPrintErrorLog() {
        String message = "Error message";
        String location = "TestClass";

        PluginLogger.error(message, location, printStream);
        String output = outContent.toString().trim();

        assertEquals("[ERROR][TestClass] Error message", output);
    }
}