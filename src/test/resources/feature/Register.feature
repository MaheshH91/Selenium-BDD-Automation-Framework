Feature: User Registration Functionality

  Background:
    Given User is on the Register page

  @Smoke @Regression
  Scenario: Verify registering an account by providing only mandatory fields
    When User registers with valid mandatory fields
    Then Account success page should display "Your Account Has Been Created!"

  @DataTables @Regression
  Scenario: Verify registering with complete dataset using Cucumber DataTable
    When User registers with the following user profile:
      | firstName | lastName | telephone  | password | subscribeNewsletter |
      | Mahesh    | Holkar   | 9876543210 | Test@123 | yes                 |
    Then Account success page should display "Your Account Has Been Created!"

  @Regression
  Scenario: Verify registering an account with already registered email
    When User registers using an existing email address
    Then An alert message saying "Warning: E-Mail Address is already registered!" should be displayed

  @Regression
  Scenario: Verify registering an account without providing any details
    When User clicks on Continue button without filling details
    Then All mandatory field warning messages should be displayed