package com.tutorialsninja.qa.drivers;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() {
        return DRIVER_THREAD.get();
    }

    public static void setDriver(String browserName, boolean isHeadless) {
        WebDriver driver;
        switch (browserName.toLowerCase().trim()) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (isHeadless) {
                    options.addArguments("-headless");
                    options.addArguments("--width=1920");
                    options.addArguments("--height=1080");
                }
                driver = new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (isHeadless) {
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1080");
                }
                driver = new EdgeDriver(options);
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

                // Anti-detection & performance flags
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                options.setExperimentalOption("prefs", prefs);

                if (isHeadless) {
                    options.addArguments("--headless=new");
                    // Force desktop dimensions so mobile responsive mode is never triggered
                    options.addArguments("--window-size=1920,1080");
                }
                driver = new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
        DRIVER_THREAD.set(driver);
    }

    public static void quitDriver() {
        if (DRIVER_THREAD.get() != null) {
            DRIVER_THREAD.get().quit();
            DRIVER_THREAD.remove();
        }
    }
}