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
        try {
            if (GraphicsEnvironment.isHeadless() || !Desktop.isDesktopSupported()) {
                logger.info("Headless environment detected; skipping auto-launch.");
                return;
            }

            // Path to the Extent Cucumber BDD report
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