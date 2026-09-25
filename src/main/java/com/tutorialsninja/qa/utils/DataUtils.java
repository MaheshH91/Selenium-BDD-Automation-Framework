package com.tutorialsninja.qa.utils;

public final class DataUtils {
    private DataUtils() {}

    public static String generateUniqueEmail() {
        return "auto_" + System.currentTimeMillis() + "@gmail.com";
    }
}