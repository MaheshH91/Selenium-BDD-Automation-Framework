package com.tutorialsninja.qa.stepdefinitions;

import java.util.List;
import java.util.Map;

import org.testng.Assert;

import com.tutorialsninja.qa.drivers.DriverManager;
import com.tutorialsninja.qa.pages.AccountSuccessPage;
import com.tutorialsninja.qa.pages.HomePage;
import com.tutorialsninja.qa.pages.RegisterPage;
import com.tutorialsninja.qa.utils.ConfigReader;
import com.tutorialsninja.qa.utils.DataUtils;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RegisterSteps {

    private RegisterPage registerPage;
    private AccountSuccessPage successPage;

    @Given("User is on the Register page")
    public void user_is_on_the_register_page() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        registerPage = homePage.navigateToRegisterPage();
    }

    @When("User registers with valid mandatory fields")
    public void user_registers_with_valid_mandatory_fields() {
        successPage = registerPage.registerMandatory(
                ConfigReader.get("firstName"),
                ConfigReader.get("lastName"),
                DataUtils.generateUniqueEmail(),
                ConfigReader.get("telephone"),
                ConfigReader.get("validPassword")
        );
    }

    @When("User registers with the following user profile:")
    public void user_registers_with_the_following_user_profile(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> user = data.get(0);

        if ("yes".equalsIgnoreCase(user.get("subscribeNewsletter"))) {
            successPage = registerPage.registerFull(
                    user.get("firstName"),
                    user.get("lastName"),
                    DataUtils.generateUniqueEmail(),
                    user.get("telephone"),
                    user.get("password")
            );
        } else {
            successPage = registerPage.registerMandatory(
                    user.get("firstName"),
                    user.get("lastName"),
                    DataUtils.generateUniqueEmail(),
                    user.get("telephone"),
                    user.get("password")
            );
        }
    }

    @Then("Account success page should display {string}")
    public void account_success_page_should_display(String expectedHeading) {
        Assert.assertEquals(successPage.getAccountSuccessHeading(), expectedHeading);
    }

    @When("User registers using an existing email address")
    public void user_registers_using_an_existing_email_address() {
        registerPage.registerFull(
                ConfigReader.get("firstName"),
                ConfigReader.get("lastName"),
                ConfigReader.get("validEmail"),
                ConfigReader.get("telephone"),
                ConfigReader.get("validPassword")
        );
    }

    @Then("An alert message saying {string} should be displayed")
    public void an_alert_message_saying_should_be_displayed(String expectedAlert) {
        Assert.assertTrue(registerPage.getAlertWarningText().contains(expectedAlert),
                "Alert message mismatch. Actual: " + registerPage.getAlertWarningText());
    }

    @When("User clicks on Continue button without filling details")
    public void user_clicks_on_continue_button_without_filling_details() {
        registerPage.clickContinue();
    }

    @Then("All mandatory field warning messages should be displayed")
    public void all_mandatory_field_warning_messages_should_be_displayed() {
        Assert.assertTrue(registerPage.areAllFieldValidationErrorsDisplayed(
                ConfigReader.get("privacyPolicyWarning"),
                ConfigReader.get("firstNameWarning"),
                ConfigReader.get("lastNameWarning"),
                ConfigReader.get("emailWarning"),
                ConfigReader.get("telephoneWarning"),
                ConfigReader.get("passwordWarning")
        ));
    }
}