# Selenium BDD Automation Framework

An enterprise-grade End-to-End Test Automation Framework built with **Java 21**, **Selenium WebDriver**, **Cucumber BDD**, and **TestNG**.

## Tech Stack
* **Language:** Java 21
* **Automation Tool:** Selenium WebDriver 4.29.0
* **BDD Framework:** Cucumber 7.18.1
* **Test Runner:** TestNG 7.10.2
* **Reporting:** ExtentReports (Grasshopper Cucumber 7 Adapter)
* **Build Tool:** Apache Maven
* **Design Pattern:** Page Object Model (POM) with ThreadLocal WebDriver

## Project Structure
```text
Selenium-BDD-Automation-Framework
├── src/main/java/com/tutorialsninja/qa
│   ├── drivers/        # ThreadLocal WebDriver manager
│   ├── pages/          # Encapsulated Page Objects
│   └── utils/          # Config & test data utilities
├── src/test/java/com/tutorialsninja/qa
│   ├── hooks/          # Scenario lifecycle & failure screenshot hooks
│   ├── listeners/      # TestNG suite & retry analyzers
│   ├── runners/        # Cucumber TestNG runner
│   └── stepdefinitions/# Step definitions & custom DataTable mappings
└── src/test/resources
    ├── config/         # System configurations (.properties)
    ├── features/       # Gherkin feature files
    ├── extent.properties
    └── testng.xml
```

## Running Tests
Run all scenarios:
```bash
mvn clean test
```

Run specific tags:
```bash
mvn test -Dcucumber.filter.tags=@Smoke
mvn test -Dcucumber.filter.tags=@Regression
```

Run on a specific browser:
```bash
mvn test -Dbrowser=firefox
```

## Reports
HTML execution reports with step-by-step logs and embedded failure screenshots are generated at:
`reports/ExtentCucumberReport.html`