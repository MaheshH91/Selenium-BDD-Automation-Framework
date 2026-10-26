package com.tutorialsninja.qa.test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.tutorialsninja.qa.base.BaseTest;
import com.tutorialsninja.qa.page.AccountPage;
import com.tutorialsninja.qa.page.HomePage;
import com.tutorialsninja.qa.page.LoginPage;
import com.tutorialsninja.qa.util.ConfigReader;
import com.tutorialsninja.qa.util.DataUtils;
import com.tutorialsninja.qa.util.ExcelUtils;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginExcelData")
    public Object[][] getLoginData() {
        return ExcelUtils.getTestData("testdata/TutorialsNinjaTestData.xlsx", "Login");
    }

    @Test(priority = 1, dataProvider = "loginExcelData")
    public void verifyLoginWithValidCredentials(String email, String password) {
        AccountPage accountPage = new HomePage(getDriver())
                .navigateToLoginPage()
                .login(email, password);

        Assert.assertTrue(accountPage.verifySuccessfulLogin(), "Login failed for valid user: " + email);
    }

    @Test(priority = 2)
    public void verifyLoginWithInvalidCredentials() {
        LoginPage loginPage = new HomePage(getDriver()).navigateToLoginPage();
        loginPage.login(DataUtils.generateUniqueEmail(), ConfigReader.get("invalidPassword"));

        Assert.assertTrue(loginPage.getWarningMessageText().contains(ConfigReader.get("emailPasswordNoMatchWarning")));
    }

    @Test(priority = 3)
    public void verifyLoginWithoutCredentials() {
        LoginPage loginPage = new HomePage(getDriver()).navigateToLoginPage();
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.getWarningMessageText().contains(ConfigReader.get("emailPasswordNoMatchWarning")));
    }
}