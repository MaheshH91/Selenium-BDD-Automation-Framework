package com.tutorialsninja.qa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.tutorialsninja.qa.base.BaseTest;
import com.tutorialsninja.qa.pages.AccountSuccessPage;
import com.tutorialsninja.qa.pages.HomePage;
import com.tutorialsninja.qa.pages.RegisterPage;
import com.tutorialsninja.qa.utils.ConfigReader;
import com.tutorialsninja.qa.utils.DataUtils;

public class RegisterTest extends BaseTest {

    @Test(priority = 1)
    public void verifyRegisterWithMandatoryFields() {
        AccountSuccessPage successPage = new HomePage(getDriver())
                .navigateToRegisterPage()
                .registerMandatory(
                        ConfigReader.get("firstName"),
                        ConfigReader.get("lastName"),
                        DataUtils.generateUniqueEmail(),
                        ConfigReader.get("telephone"),
                        ConfigReader.get("validPassword")
                );

        Assert.assertEquals(successPage.getAccountSuccessHeading(), ConfigReader.get("accountSuccessfullyCreatedHeading"));
    }

    @Test(priority = 2)
    public void verifyRegisterWithExistingEmail() {
        RegisterPage registerPage = new HomePage(getDriver()).navigateToRegisterPage();
        registerPage.registerFull(
                ConfigReader.get("firstName"),
                ConfigReader.get("lastName"),
                ConfigReader.get("validEmail"),
                ConfigReader.get("telephone"),
                ConfigReader.get("validPassword")
        );

        Assert.assertTrue(registerPage.getAlertWarningText().contains(ConfigReader.get("duplicateEmailWarning")));
    }

    @Test(priority = 3)
    public void verifyRegisterWithoutDetails() {
        RegisterPage registerPage = new HomePage(getDriver()).navigateToRegisterPage();
        registerPage.clickContinue();

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