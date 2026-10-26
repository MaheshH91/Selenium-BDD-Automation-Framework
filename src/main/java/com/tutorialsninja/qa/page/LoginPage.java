package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(id = "input-email")
    private WebElement emailField;

    @FindBy(id = "input-password")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@value='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[contains(@class,'alert-danger')]")
    private WebElement warningMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public AccountPage login(String email, String password) {
        type(emailField, email);
        type(passwordField, password);
        click(loginButton);
        return new AccountPage(driver);
    }

    public void clickLogin() {
        click(loginButton);
    }

    public String getWarningMessageText() {
        return getText(warningMessage);
    }
}