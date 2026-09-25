package com.tutorialsninja.qa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.tutorialsninja.qa.base.BaseTest;
import com.tutorialsninja.qa.pages.HomePage;
import com.tutorialsninja.qa.pages.SearchPage;
import com.tutorialsninja.qa.utils.ConfigReader;

public class SearchTest extends BaseTest {

    @Test(priority = 1)
    public void verifySearchWithValidProduct() {
        SearchPage searchPage = new HomePage(getDriver())
                .searchProduct(ConfigReader.get("validProduct"));

        Assert.assertTrue(searchPage.displayStatusOfHPValidProduct(), "Expected valid product is missing.");
    }

    @Test(priority = 2)
    public void verifySearchWithInvalidProduct() {
        SearchPage searchPage = new HomePage(getDriver())
                .searchProduct(ConfigReader.get("invalidProduct"));

        Assert.assertEquals(searchPage.retrieveNoProductMessageText(), ConfigReader.get("NoProductInSearchResult"));
    }

    @Test(priority = 3, dependsOnMethods = {"verifySearchWithInvalidProduct"})
    public void verifySearchWithoutAnyProduct() {
        SearchPage searchPage = new HomePage(getDriver())
                .clickSearchButton();

        Assert.assertEquals(searchPage.retrieveNoProductMessageText(), ConfigReader.get("NoProductInSearchResult"));
    }
}