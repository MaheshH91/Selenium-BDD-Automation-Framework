package com.tutorialsninja.qa.util;

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
    private static final ThreadLocal<ExtentTest> TEST_THREAD = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static synchronized ExtentReports getReportInstance() {
        if (extentReports == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File reportsDir = new File(System.getProperty("user.dir") + File.separator + "reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            reportFilePath = Paths.get(reportsDir.getAbsolutePath(), "ExtentReport_" + timestamp + ".html").toString();

            ExtentSparkReporter reporter = new ExtentSparkReporter(reportFilePath);
            reporter.config().setReportName("Tutorials Ninja Automation Results");
            reporter.config().setDocumentTitle("Test Execution Report");
            reporter.config().setTheme(Theme.DARK);

            extentReports = new ExtentReports();
            extentReports.attachReporter(reporter);
            extentReports.setSystemInfo("URL", ConfigReader.getOrDefault("url", "N/A"));
            extentReports.setSystemInfo("Browser", ConfigReader.getOrDefault("browser", "N/A"));
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
        return extentReports;
    }

    public static String getReportPath() {
        return reportFilePath;
    }

    public static ExtentTest getTest() {
        return TEST_THREAD.get();
    }

    public static void setTest(ExtentTest test) {
        TEST_THREAD.set(test);
    }

    public static void unloadTest() {
        TEST_THREAD.remove();
    }
}