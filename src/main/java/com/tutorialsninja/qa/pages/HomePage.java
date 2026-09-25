package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy(xpath = "//span[normalize-space()='My Account']")
    private WebElement myAccountMenu;

    @FindBy(linkText = "Login")
    private WebElement loginOption;

    @FindBy(linkText = "Register")
    private WebElement registerOption;

    @FindBy(name = "search")
    private WebElement searchField;

    @FindBy(xpath = "//div[@id='search']//button")
    private WebElement searchButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public LoginPage navigateToLoginPage() {
        click(myAccountMenu);
        click(loginOption);
        return new LoginPage(driver);
    }

    public RegisterPage navigateToRegisterPage() {
        click(myAccountMenu);
        click(registerOption);
        return new RegisterPage(driver);
    }

    public SearchPage searchProduct(String productName) {
        type(searchField, productName);
        click(searchButton);
        return new SearchPage(driver);
    }

    public SearchPage clickSearchButton() {
        click(searchButton);
        return new SearchPage(driver);
    }
}