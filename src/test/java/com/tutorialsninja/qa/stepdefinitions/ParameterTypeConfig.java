package com.tutorialsninja.qa.stepdefinitions;


import io.cucumber.java.ParameterType;

public class ParameterTypeConfig {

    // Matches valid email addresses directly in Gherkin steps
    @ParameterType("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    public String emailAddress(String email) {
        return email;
    }
}