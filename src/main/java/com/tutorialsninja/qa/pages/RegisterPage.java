package com.tutorialsninja.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegisterPage extends BasePage {

    @FindBy(id = "input-firstname")
    private WebElement firstNameField;

    @FindBy(id = "input-lastname")
    private WebElement lastNameField;

    @FindBy(id = "input-email")
    private WebElement emailField;

    @FindBy(id = "input-telephone")
    private WebElement telephoneField;

    @FindBy(id = "input-password")
    private WebElement passwordField;

    @FindBy(id = "input-confirm")
    private WebElement confirmPasswordField;

    @FindBy(xpath = "//input[@name='newsletter'][@value='1']")
    private WebElement newsletterSubscribeRadio;

    @FindBy(name = "agree")
    private WebElement privacyPolicyCheckbox;

    @FindBy(xpath = "//input[@value='Continue']")
    private WebElement continueButton;

    @FindBy(xpath = "//div[contains(@class,'alert-danger')]")
    private WebElement alertWarning;

    @FindBy(xpath = "//input[@id='input-firstname']/following-sibling::div")
    private WebElement firstNameError;

    @FindBy(xpath = "//input[@id='input-lastname']/following-sibling::div")
    private WebElement lastNameError;

    @FindBy(xpath = "//input[@id='input-email']/following-sibling::div")
    private WebElement emailError;

    @FindBy(xpath = "//input[@id='input-telephone']/following-sibling::div")
    private WebElement telephoneError;

    @FindBy(xpath = "//input[@id='input-password']/following-sibling::div")
    private WebElement passwordError;

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    private void fillAccountDetails(String firstName, String lastName, String email, String phone, String password) {
        type(firstNameField, firstName);
        type(lastNameField, lastName);
        type(emailField, email);
        type(telephoneField, phone);
        type(passwordField, password);
        type(confirmPasswordField, password);
        click(privacyPolicyCheckbox);
    }

    public AccountSuccessPage registerMandatory(String fn, String ln, String email, String phone, String pwd) {
        fillAccountDetails(fn, ln, email, phone, pwd);
        click(continueButton);
        return new AccountSuccessPage(driver);
    }

    public AccountSuccessPage registerFull(String fn, String ln, String email, String phone, String pwd) {
        fillAccountDetails(fn, ln, email, phone, pwd);
        click(newsletterSubscribeRadio);
        click(continueButton);
        return new AccountSuccessPage(driver);
    }

    public void clickContinue() {
        click(continueButton);
    }

    public String getAlertWarningText() {
        return getText(alertWarning);
    }

    public boolean areAllFieldValidationErrorsDisplayed(String privacy, String fn, String ln, String email, String phone, String pwd) {
        return getText(alertWarning).contains(privacy)
                && getText(firstNameError).contains(fn)
                && getText(lastNameError).contains(ln)
                && getText(emailError).contains(email)
                && getText(telephoneError).contains(phone)
                && getText(passwordError).contains(pwd);
    }
}