
package com.tutorialsninja.qa.context;

import com.tutorialsninja.qa.driver.DriverManager;
import com.tutorialsninja.qa.page.AccountPage;
import com.tutorialsninja.qa.page.HomePage;
import com.tutorialsninja.qa.page.LoginPage;
import com.tutorialsninja.qa.page.RegisterPage;
import com.tutorialsninja.qa.page.SearchPage;

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

    public RegisterPage getRegisterPage() {
        return (registerPage == null) ? new RegisterPage(DriverManager.getDriver()) : registerPage;
    }

    public SearchPage getSearchPage() {
        return (searchPage == null) ? new SearchPage(DriverManager.getDriver()) : searchPage;
    }

    public AccountPage getAccountPage() {
        return accountPage;
    }

    public void setAccountPage(AccountPage accountPage) {
        this.accountPage = accountPage;
    }

    public void setSearchPage(SearchPage searchPage) {
        this.searchPage = searchPage;
    }

    public void setRegisterPage(RegisterPage registerPage) {
        this.registerPage = registerPage;
    }
}