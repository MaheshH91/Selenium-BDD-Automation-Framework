package com.tutorialsninja.qa.context;

import com.tutorialsninja.qa.pages.*;
import com.tutorialsninja.qa.drivers.DriverManager;

public class TestContext {
    private HomePage homePage;
    private LoginPage loginPage;
    private AccountPage accountPage;
    private RegisterPage registerPage;
    private SearchPage searchPage;

    public HomePage getHomePage() {
        return (homePage == null) ? new HomePage(DriverManager.getDriver()) : homePage;
    }

    public LoginPage getLoginPage() {
        return (loginPage == null) ? new LoginPage(DriverManager.getDriver()) : loginPage;
    }

    public AccountPage getAccountPage() {
        return accountPage;
    }

    public void setAccountPage(AccountPage accountPage) {
        this.accountPage = accountPage;
    }
}