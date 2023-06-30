package io.jenkins.plugins.kobiton.config;

import io.jenkins.plugins.kobiton.shared.utils.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigManager {
    private final Properties properties;

    ConfigManager(Properties properties) {
        this.properties = properties;
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public List<String> getArrayProperty(String key, String[] defaultValues) {
        String value = getProperty(key, "");
        if (StringUtils.isNullOrEmpty(value)) {
            return Arrays.asList(defaultValues);
        }
        return Arrays.asList(value.split(","));
    }

    public static class Builder {
        private static final String DEFAULT_DIR_PATH = "build-config/";
        private static final String DEFAULT_FILE_PATH = DEFAULT_DIR_PATH + "config.properties";
        private String filePath;

        public Builder() {
            this.filePath = DEFAULT_FILE_PATH;
        }

        public Builder filePath(String configFileName) {
            this.filePath = DEFAULT_DIR_PATH + configFileName;
            return this;
        }

        public ConfigManager build() {
            Properties properties = new Properties();

            try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                properties.load(fileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new ConfigManager(properties);
        }
    }
}
