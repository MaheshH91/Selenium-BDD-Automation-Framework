@Search
Feature: Product Search Functionality

  Background:
    Given User is on the Home page

  @Smoke @Regression
  Scenario: Verify searching with an existing valid product
    When User searches for valid product "HP LP3065"
    Then Product "HP LP3065" should be displayed in the search results

  @Regression
  Scenario: Verify searching with a non-existing product
    When User searches for invalid product "Fitbit"
    Then Search result should display message "There is no product that matches the search criteria."

  @Regression
  Scenario: Verify searching without providing any product name
    When User clicks on the search button without product text
    Then Search result should display message "There is no product that matches the search criteria."