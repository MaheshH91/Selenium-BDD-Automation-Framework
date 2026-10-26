package com.tutorialsninja.qa.stepdefinition;

import org.testng.Assert;

import com.tutorialsninja.qa.context.TestContext;
import com.tutorialsninja.qa.driver.DriverManager;
import com.tutorialsninja.qa.page.HomePage;
import com.tutorialsninja.qa.page.SearchPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchStepDefinitions {

    private HomePage homePage;
    private SearchPage searchPage;
    private final TestContext context;

    public SearchStepDefinitions(TestContext context) {
        this.context = context;
    }

    @Given("User is on the Home page")
    public void user_is_on_the_home_page() {
        homePage = new HomePage(DriverManager.getDriver());
    }

    @When("User searches for valid product {string}")
    public void user_searches_for_valid_product(String productName) {
        searchPage = homePage.searchProduct(productName);
    }

    @Then("Product {string} should be displayed in the search results")
    public void product_should_be_displayed_in_the_search_results(String productName) {
        Assert.assertTrue(searchPage.displayStatusOfHPValidProduct(), 
                "Expected product '" + productName + "' is not displayed in search results.");
    }

    @When("User searches for invalid product {string}")
    public void user_searches_for_invalid_product(String productName) {
        searchPage = homePage.searchProduct(productName);
    }

    @Then("Search result should display message {string}")
    public void search_result_should_display_message(String expectedMessage) {
        Assert.assertEquals(searchPage.retrieveNoProductMessageText(), expectedMessage,
                "Search result message does not match expected text.");
    }

    @When("User clicks on the search button without product text")
    public void user_clicks_on_the_search_button_without_product_text() {
        searchPage = homePage.clickSearchButton();
    }
}