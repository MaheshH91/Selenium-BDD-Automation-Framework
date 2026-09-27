package com.tutorialsninja.qa.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

public final class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {}

    public static JsonNode readJsonFile(String relativePath) {
        try (InputStream stream = JsonUtils.class.getClassLoader().getResourceAsStream(relativePath)) {
            if (stream == null) {
                throw new IllegalArgumentException("JSON file not found on classpath: " + relativePath);
            }
            return mapper.readTree(stream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON data from: " + relativePath, e);
        }
    }

    public static JsonNode getUserData(String filePath, String arrayName, int index) {
        JsonNode root = readJsonFile(filePath);
        return root.get(arrayName).get(index);
    }
}