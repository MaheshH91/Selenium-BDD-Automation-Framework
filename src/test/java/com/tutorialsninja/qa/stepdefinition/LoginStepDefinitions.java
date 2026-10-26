package com.tutorialsninja.qa.stepdefinition;

import org.testng.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.tutorialsninja.qa.context.TestContext;
import com.tutorialsninja.qa.driver.DriverManager;
import com.tutorialsninja.qa.page.AccountPage;
import com.tutorialsninja.qa.page.HomePage;
import com.tutorialsninja.qa.page.LoginPage;
import com.tutorialsninja.qa.util.ConfigReader;
import com.tutorialsninja.qa.util.DataUtils;
import com.tutorialsninja.qa.util.JsonUtils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginStepDefinitions {

    private LoginPage loginPage;
    private AccountPage accountPage;
    private final TestContext context;

    public LoginStepDefinitions(TestContext context) {
        this.context = context;
    }

    @Given("User is on the Login page")
    public void user_is_on_the_login_page() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        loginPage = homePage.navigateToLoginPage();
    }

    @When("User logs in using email {string} and password {string}")
    public void user_logs_in_using_email_and_password(String email, String password) {
        accountPage = loginPage.login(email, password);
        context.setAccountPage(accountPage);
    }

    @Then("User should be navigated to Account page and verify login status")
    public void user_should_be_navigated_to_account_page_and_verify_login_status() {
        AccountPage targetAccountPage = (accountPage != null) ? accountPage : context.getAccountPage();
        Assert.assertNotNull(targetAccountPage, "AccountPage instance was not initialized.");
        Assert.assertTrue(targetAccountPage.verifySuccessfulLogin(), "Login verification failed on Account page.");
    }

    @When("User logs in using an invalid email and invalid password")
    public void user_logs_in_using_an_invalid_email_and_invalid_password() {
        loginPage.login(DataUtils.generateUniqueEmail(), ConfigReader.get("invalidPassword"));
    }

    @When("User clicks on the Login button without credentials")
    public void user_clicks_on_the_login_button_without_credentials() {
        loginPage.clickLogin();
    }

    @When("User logs in using credentials from JSON {string} under {string} at index {int}")
    public void user_logs_in_using_credentials_from_json(String filePath, String key, int index) {
        JsonNode userNode = JsonUtils.getUserData(filePath, key, index);

        if (userNode == null || !userNode.hasNonNull("email") || !userNode.hasNonNull("password")) {
            throw new IllegalArgumentException(
                String.format("Missing 'email' or 'password' in JSON '%s' under key '%s' at index %d",
                        filePath, key, index)
            );
        }

        String email = userNode.get("email").asText();
        String password = userNode.get("password").asText();

        accountPage = loginPage.login(email, password);
        context.setAccountPage(accountPage);

        loginPage = new LoginPage(DriverManager.getDriver());
    }

    @Then("A warning message saying {string} should be displayed")
    public void a_warning_message_saying_should_be_displayed(String expectedWarning) {
        Assert.assertTrue(loginPage.getWarningMessageText().contains(expectedWarning),
                "Expected warning not found. Actual: " + loginPage.getWarningMessageText());
    }
}