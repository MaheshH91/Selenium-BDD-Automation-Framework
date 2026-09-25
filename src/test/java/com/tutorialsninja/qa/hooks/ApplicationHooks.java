package com.tutorialsninja.qa.hooks;


import java.time.Duration;

import com.tutorialsninja.qa.drivers.DriverManager;
import com.tutorialsninja.qa.utils.ConfigReader;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ApplicationHooks {

    private static final Logger logger = LogManager.getLogger(ApplicationHooks.class);

    @Before(order = 0)
    public void initDriver(Scenario scenario) {
        String browser = ConfigReader.get("browser");
        boolean headless = ConfigReader.getBoolean("headless");

        DriverManager.setDriver(browser, headless);
        WebDriver driver = DriverManager.getDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicitWait")));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("pageLoadTimeout")));
        driver.get(ConfigReader.get("url"));

        logger.info("Executing Scenario: [{}] on Browser: [{}]", scenario.getName(), browser);
    }

    @After(order = 1)
    public void tearDownWithScreenshot(Scenario scenario) {
        if (scenario.isFailed()) {
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure_Evidence");
                logger.error("Scenario [{}] failed. Screenshot captured.", scenario.getName());
            }
        }
    }

    @After(order = 0)
    public void quitBrowser() {
        DriverManager.quitDriver();
        logger.info("Driver destroyed cleanly.");
    }
}