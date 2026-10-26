package com.tutorialsninja.qa.listeners;

import com.tutorialsninja.qa.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int count = 0;
    private final int maxTry;

    public RetryAnalyzer() {
        int configuredRetries = 1;
        try {
            configuredRetries = ConfigReader.getInt("maxRetryCount");
        } catch (Exception e) {
            logger.warn("Property 'maxRetryCount' not defined; defaulting to 1 retry attempt.");
        }
        this.maxTry = configuredRetries;
    }

    @Override
    public boolean retry(ITestResult result) {
        if (count < maxTry) {
            count++;
            logger.warn("Retrying test '{}' (Attempt {}/{})", result.getName(), count, maxTry);
            return true;
        }
        return false;
    }
}