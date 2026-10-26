@Register
Feature: Registration Functionality

  Background:
    Given User is on the Register page

  @Smoke @Regression
  Scenario: Verify registration with mandatory fields
    When User registers with valid mandatory fields
    Then Account success page should display "Your Account Has Been Created!"

  @DataTables @Regression
  Scenario: Verify registration with complete details using DataTable
    When User registers with the following user profile:
      | firstName | lastName | telephone  | password | subscribeNewsletter |
      | John      | Doe      | 9876543210 | Test@123 | yes                 |
    Then Account success page should display "Your Account Has Been Created!"

  @Negative @Regression
  Scenario: Verify registration with an already registered email address
    When User registers using an existing email address
    Then An alert message saying "Warning: E-Mail Address is already registered!" should be displayed

  @Negative @Regression
  Scenario: Verify registration without providing any details
    When User clicks on Continue button without filling details
    Then All mandatory field warning messages should be displayed