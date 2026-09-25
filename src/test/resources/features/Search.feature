Feature: Product Search Functionality

  Background:
    Given User is on the Home page

  @Smoke @Regression
  Scenario: Verify search with existing product
    When User searches for valid product "HP"
    Then Product "HP LP3065" should be displayed in the search results

  @Regression
  Scenario: Verify search with non-existing product
    When User searches for invalid product "Honda"
    Then Search result should display message "There is no product that matches the search criteria."

  @Regression
  Scenario: Verify search without entering any product
    When User clicks on the search button without product text
    Then Search result should display message "There is no product that matches the search criteria."