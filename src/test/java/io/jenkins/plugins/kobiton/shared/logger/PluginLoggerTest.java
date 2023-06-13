package io.jenkins.plugins.kobiton.shared.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class PluginLoggerTest {
    private ByteArrayOutputStream outContent;
    private PrintStream printStream;
    private static MemoryAppender memoryAppender;

    @BeforeEach
    public void setup() {
        outContent = new ByteArrayOutputStream();
        printStream = new PrintStream(outContent);
    }

    @Test
    void PluginLogger_Constructor_IsPrivate() throws NoSuchMethodException {
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

    @Nested
    class LoggerTest{
        @BeforeEach
        public void setUp() {
            Logger logger = (Logger) LoggerFactory.getLogger(PluginLogger.class);
            memoryAppender = new MemoryAppender();
            memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
            logger.setLevel(Level.DEBUG);
            logger.addAppender(memoryAppender);
            memoryAppender.start();
        }

        @AfterEach
        public void tearDown() {
            memoryAppender.reset();
            memoryAppender.stop();
        }

        @Test
        void test() {
            boolean expected = true;
            String message = "Debug message";
            String location = "TestClass";

            PluginLogger.debug(message, location);

            assertEquals(expected, memoryAppender.search("Debug message", Level.INFO).get(0).toString().contains("[TestClass] Debug message"));
        }
    }
}