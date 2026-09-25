package com.tutorialsninja.qa.stepdefinitions;

import org.testng.Assert;
import com.tutorialsninja.qa.drivers.DriverManager;
import com.tutorialsninja.qa.pages.AccountPage;
import com.tutorialsninja.qa.pages.HomePage;
import com.tutorialsninja.qa.pages.LoginPage;
import com.tutorialsninja.qa.utils.ConfigReader;
import com.tutorialsninja.qa.utils.DataUtils;
import io.cucumber.java.en.*;

public class LoginSteps {

    private LoginPage loginPage;
    private AccountPage accountPage;

    @Given("User is on the Login page")
    public void user_is_on_the_login_page() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        loginPage = homePage.navigateToLoginPage();
    }

    @When("User logs in using email {string} and password {string}")
    public void user_logs_in_using_email_and_password(String email, String password) {
        accountPage = loginPage.login(email, password);
    }

    @Then("User should be navigated to Account page and verify login status")
    public void user_should_be_navigated_to_account_page_and_verify_login_status() {
        Assert.assertTrue(accountPage.verifySuccessfulLogin(), "Login verification failed on Account page.");
    }

    @When("User logs in using an invalid email and invalid password")
    public void user_logs_in_using_an_invalid_email_and_invalid_password() {
        loginPage.login(DataUtils.generateUniqueEmail(), ConfigReader.get("invalidPassword"));
    }

    @When("User clicks on the Login button without credentials")
    public void user_clicks_on_the_login_button_without_credentials() {
        loginPage.clickLogin();
    }

    @Then("A warning message saying {string} should be displayed")
    public void a_warning_message_saying_should_be_displayed(String expectedWarning) {
        Assert.assertTrue(loginPage.getWarningMessageText().contains(expectedWarning),
                "Expected warning not found. Actual: " + loginPage.getWarningMessageText());
    }
}