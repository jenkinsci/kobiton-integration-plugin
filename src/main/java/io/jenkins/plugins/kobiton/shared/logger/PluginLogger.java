package io.jenkins.plugins.kobiton.shared.logger;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PluginLogger {
    private static final Logger logger = Logger.getLogger(PluginLogger.class.getName());
    private PluginLogger() {
        throw new IllegalStateException("Utils class");
    }

    public static void debug(String message, String location) {
        logger.log(Level.SEVERE, "[{0}] {1} ", new Object[]{location, message});
    }

    public static void log(String message, PrintStream printStream) {
        printStream.println(message);
    }

    public static void error(String message, String location, PrintStream printStream) {
        printStream.println("[ERROR]" + "[" + location + "] " + message);
    }
}
