package io.jenkins.plugins.kobiton.shared.logger;

import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PluginLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginLogger.class);
    private PluginLogger() {
        throw new IllegalStateException("Utils class");
    }

    public static void debug(String message, String location) {
        LOGGER.info("[{}] {}", location, message);
    }

    public static void log(String message, PrintStream printStream) {
        printStream.println(message);
    }

    public static void error(String message, String location, PrintStream printStream) {
        printStream.println("[ERROR]" + "[" + location + "] " + message);
    }
}
