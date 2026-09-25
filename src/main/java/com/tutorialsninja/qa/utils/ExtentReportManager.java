package com.tutorialsninja.qa.utils;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public final class ExtentReportManager {

    private static ExtentReports extentReports;
    private static String reportFilePath;
    private static final ThreadLocal<ExtentTest> EXTENT_TEST = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static synchronized ExtentReports getReportInstance() {
        if (extentReports == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            reportFilePath = Paths.get(System.getProperty("user.dir"), "reports", "ExtentReport_" + timestamp + ".html").toString();

            ExtentSparkReporter reporter = new ExtentSparkReporter(new File(reportFilePath));
            reporter.config().setReportName("Tutorials Ninja Automation Results");
            reporter.config().setDocumentTitle("Test Execution Report");
            reporter.config().setTheme(Theme.DARK);

            extentReports = new ExtentReports();
            extentReports.attachReporter(reporter);
            extentReports.setSystemInfo("URL", ConfigReader.get("url"));
            extentReports.setSystemInfo("Browser", ConfigReader.get("browser"));
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java", System.getProperty("java.version"));
        }
        return extentReports;
    }

    public static String getReportPath() {
        return reportFilePath;
    }

    public static ExtentTest getTest() {
        return EXTENT_TEST.get();
    }

    public static void setTest(ExtentTest test) {
        EXTENT_TEST.set(test);
    }

    public static void unloadTest() {
        EXTENT_TEST.remove();
    }
}