package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AccountPage extends BasePage {

    @FindBy(xpath = "//h2[text()='My Account']")
    private WebElement myAccountHeading;

    @FindBy(linkText = "Logout")
    private WebElement logoutLink;

    public AccountPage(WebDriver driver) {
        super(driver);
    }

    public boolean verifySuccessfulLogin() {
        return isElementDisplayed(myAccountHeading);
    }

    public void clickLogout() {
        click(logoutLink);
    }
}