package com.tutorialsninja.qa.base;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.tutorialsninja.qa.drivers.DriverManager;
import com.tutorialsninja.qa.utils.ConfigReader;

public abstract class BaseTest {

    protected final Logger logger = LogManager.getLogger(getClass());

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(@Optional String browserParam) {
        String targetBrowser = (browserParam != null && !browserParam.isBlank())
                ? browserParam
                : ConfigReader.get("browser");

        boolean isHeadless = ConfigReader.getBoolean("headless");

        DriverManager.setDriver(targetBrowser, isHeadless);
        WebDriver driver = getDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicitWait")));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("pageLoadTimeout")));
        driver.get(ConfigReader.get("url"));

        logger.info("Initialized {} driver on thread {}", targetBrowser, Thread.currentThread().getId());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
        logger.info("Terminated driver session.");
    }
}