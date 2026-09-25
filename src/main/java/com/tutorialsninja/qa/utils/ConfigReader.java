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
                throw new RuntimeException("Property file not found on classpath: " + resourcePath);
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read property file: " + resourcePath, e);
        }
    }

    public static String get(String key) {
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.isBlank()) {
            return systemProp.trim();
        }
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Key '" + key + "' was not found in properties configuration.");
        }
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}