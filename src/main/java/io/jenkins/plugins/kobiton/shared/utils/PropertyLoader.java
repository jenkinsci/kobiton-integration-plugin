package io.jenkins.plugins.kobiton.shared.utils;

import io.jenkins.plugins.kobiton.config.ConfigManager;

import java.util.List;

public class PropertyLoader {
    private PropertyLoader() { throw new IllegalStateException("Utils class"); }

    public static String loadProperty(String key, String defaultValue) {
        ConfigManager configManager = new ConfigManager.Builder().build();
        return configManager.getProperty(key, defaultValue);
    }

    public static List<String> loadProperties(String key, String[] defaultValues) {
        ConfigManager configManager = new ConfigManager.Builder().build();
        return configManager.getArrayProperty(key, defaultValues);
    }
}
