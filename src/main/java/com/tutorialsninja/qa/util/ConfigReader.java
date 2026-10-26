package com.tutorialsninja.qa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        loadProperties("config/config.properties");
        loadProperties("config/testdata.properties");
    }

    private ConfigReader() {}

    private static void loadProperties(String resourcePath) {
        try (InputStream stream = ConfigReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new IllegalStateException("Property file not found on classpath: " + resourcePath);
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read property file: " + resourcePath, e);
        }
    }

    /**
     * Resolves key by checking:
     * 1. System Properties (-Dkey=value)
     * 2. Environment Variables (KEY or key)
     * 3. Loaded classpath properties files
     */
    public static String get(String key) {
        // 1. System Property check (Maven / CLI / Jenkins CLI parameter)
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.isBlank()) {
            return systemProp.trim();
        }

        // 2. OS Environment variable check (CI/CD environments)
        String envProp = System.getenv(key.toUpperCase().replace('.', '_'));
        if (envProp != null && !envProp.isBlank()) {
            return envProp.trim();
        }

        // 3. Fallback to properties files
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Key '" + key + "' was not found in properties configuration.");
        }
        return value.trim();
    }

    public static String getOrDefault(String key, String defaultValue) {
        try {
            return get(key);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static int getIntOrDefault(String key, int defaultValue) {
        try {
            return getInt(key);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static boolean getBooleanOrDefault(String key, boolean defaultValue) {
        try {
            return getBoolean(key);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }
}