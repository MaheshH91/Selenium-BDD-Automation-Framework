Feature: User Login Functionality

  Background:
    Given User is on the Login page

  @Smoke @Regression
  Scenario Outline: Verify login with valid credentials
    When User logs in using email "<email>" and password "<password>"
    Then User should be navigated to Account page and verify login status

    Examples:
      | email                        | password  |
      | maheshpatil234@gmail.com     | 12345     |
      | mahesh.jadhav@aressindia.net | 123456789 |
      | mahesh.holkar@gmail.com      | 12345     |

  @Regression
  Scenario: Verify login with invalid credentials
    When User logs in using an invalid email and invalid password
    Then A warning message saying "Warning: No match for E-Mail Address and/or Password." should be displayed

  @Regression
  Scenario: Verify login without entering credentials
    When User clicks on the Login button without credentials
    Then A warning message saying "Warning: No match for E-Mail Address and/or Password." should be displayed