package com.tutorialsninja.qa.stepdefinitions;


import io.cucumber.java.DataTableType;
import java.util.Map;

public class DataTableTypeConfig {

    public record UserProfile(String firstName, String lastName, String telephone, 
                              String password, boolean subscribeNewsletter) {}

    @DataTableType
    public UserProfile defineUserProfile(Map<String, String> entry) {
        return new UserProfile(
                entry.get("firstName"),
                entry.get("lastName"),
                entry.get("telephone"),
                entry.get("password"),
                "yes".equalsIgnoreCase(entry.get("subscribeNewsletter"))
        );
    }
}