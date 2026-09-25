package com.tutorialsninja.qa.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.tutorialsninja.qa.drivers.DriverManager;
import com.tutorialsninja.qa.utils.ExtentReportManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentReportManager.getReportInstance().createTest(result.getMethod().getMethodName());
        ExtentReportManager.setTest(test);
        test.log(Status.INFO, "Test execution started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "Test passed: " + result.getName());
        }
        ExtentReportManager.unloadTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();

        if (result.wasRetried()) {
            if (test != null) {
                test.log(Status.WARNING, "Test failed, re-running attempt: " + result.getName());
            }
            ExtentReportManager.unloadTest();
            return;
        }

        if (test != null) {
            test.log(Status.FAIL, result.getThrowable());

            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                try {
                    String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                    test.fail("Failure Screenshot", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                } catch (Exception e) {
                    test.info("Screenshot capture unavailable: " + e.getMessage());
                }
            }
        }
        ExtentReportManager.unloadTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + result.getName());
        }
        ExtentReportManager.unloadTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        if (ExtentReportManager.getReportInstance() != null) {
            ExtentReportManager.getReportInstance().flush();
        }
    }
}