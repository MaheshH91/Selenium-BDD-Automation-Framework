package com.tutorialsninja.qa.listeners;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class SuiteListener implements ISuiteListener {

    private static final Logger logger = LogManager.getLogger(SuiteListener.class);

    @Override
    public void onStart(ISuite suite) {
        logger.info("========== Initiating Suite: {} ==========", suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        logger.info("========== Suite Finished: {} ==========", suite.getName());
        openCucumberReport();
    }

    private void openCucumberReport() {
    	// 1. Detect if running inside Jenkins or any CI runner
        boolean isCI = System.getenv("JENKINS_HOME") != null 
                    || System.getenv("CI") != null 
                    || Boolean.parseBoolean(System.getProperty("headless", "false"));

        if (isCI) {
            logger.info("CI/Headless environment detected (Jenkins). Skipping desktop report auto-launch; report is archived by pipeline.");
            return;
        }

        // 2. Standard local desktop verification
        try {
            if (GraphicsEnvironment.isHeadless() || !Desktop.isDesktopSupported()) {
                logger.info("No desktop environment supported; skipping report launch.");
                return;
            }

            String cucumberReportPath = Paths.get(System.getProperty("user.dir"), "reports", "ExtentCucumberReport.html").toString();
            File reportFile = new File(cucumberReportPath);

            if (reportFile.exists()) {
                Desktop.getDesktop().browse(reportFile.toURI());
                logger.info("Successfully launched Cucumber Extent Report: {}", cucumberReportPath);
            } else {
                logger.warn("Cucumber report not found at: {}", cucumberReportPath);
            }
        } catch (Exception e) {
            logger.warn("Could not auto-open report: {}", e.getMessage());
        }
    }
}