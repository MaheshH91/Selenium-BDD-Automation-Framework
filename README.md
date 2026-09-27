# Selenium BDD Automation Framework

An enterprise-grade **End-to-End Test Automation Framework** for scalable web application testing, built using **Java 21, Selenium WebDriver, Cucumber BDD, TestNG, and Maven**.

The framework is designed around maintainability, scalability, parallel execution, data-driven testing, reusable Page Objects, automated failure evidence, and CI/CD integration.

The framework currently targets the [TutorialsNinja Demo Application](https://tutorialsninja.com/demo/).

---

## 📌 Table of Contents

* [Overview](#-overview)
* [Tech Stack](#-tech-stack)
* [Key Features](#-key-features)
* [Framework Architecture](#-framework-architecture)
* [Project Structure](#-project-structure)
* [Configuration](#%EF%B8%8F-configuration)
* [Data-Driven Testing](#-data-driven-testing)
* [Cucumber Examples](#-cucumber-examples)
* [Running Tests](#-running-tests)
* [Parallel Execution](#-parallel-execution)
* [Retry Mechanism](#-retry-mechanism)
* [Failure Evidence](#-failure-evidence)
* [Reports and Logs](#-reports-and-logs)
* [CI/CD with Jenkins](#-cicd-with-jenkins)
* [Design Principles](#-design-principles)
* [Prerequisites](#-prerequisites)
* [Author](#-author)

---

## 🚀 Overview

This framework provides a structured approach to developing and executing automated web tests using **Behavior-Driven Development (BDD)**.

It combines:

* Selenium WebDriver for browser automation
* Cucumber for readable Gherkin scenarios
* TestNG for test execution and parallelization
* PicoContainer for dependency injection and state management
* Page Object Model for UI abstraction
* JSON and Excel for external test data
* Cucumber DataTables for scenario-level data
* ExtentReports for rich execution reporting
* Log4j2 for structured logging
* Jenkins for CI/CD execution

The architecture is designed to support multiple browsers, parallel execution, environment-specific configuration, reusable test components, and enterprise-level reporting.

---

## 🛠 Tech Stack

| Technology                | Version / Details             |
| ------------------------- | ----------------------------- |
| **Java**                  | 21                            |
| **Selenium WebDriver**    | 4.49.0                        |
| **Cucumber Java**         | 7.34.9                        |
| **Cucumber TestNG**       | 7.34.9                        |
| **TestNG**                | 7.10.2                        |
| **PicoContainer**         | Cucumber Dependency Injection |
| **Apache Maven**          | Build & dependency management |
| **Apache POI**            | 5.2.5                         |
| **Jackson Databind**      | 2.17.0                        |
| **ExtentReports Adapter** | 1.14.0                        |
| **Log4j2**                | 2.20.0                        |
| **CI/CD**                 | Jenkins                       |
| **Browsers**              | Chrome, Firefox, Edge         |

---

## ✨ Key Features

### 1. Page Object Model

The framework follows the **Page Object Model (POM)** design pattern.

Reusable page classes encapsulate:

* Locators
* UI interactions
* Explicit waits
* Page-specific operations

Core page objects include:

* `BasePage`
* `LoginPage`
* `RegisterPage`
* `SearchPage`
* `AccountPage`

A reusable `BasePage` provides common Selenium operations and explicit wait synchronization.

---

### 2. Thread-Safe Parallel Execution

Parallel browser execution is supported using:

```java
ThreadLocal<WebDriver>
```

The `DriverManager` maintains an isolated WebDriver instance for each execution thread.

TestNG parallel execution is configured using:

```java
@DataProvider(parallel = true)
```

This allows multiple scenarios to execute simultaneously without sharing browser sessions between threads.

---

### 3. Multi-Browser Support

The framework supports:

* Google Chrome
* Mozilla Firefox
* Microsoft Edge

Browser selection can be controlled through Maven properties:

```bash
mvn test -Dbrowser=chrome
```

```bash
mvn test -Dbrowser=firefox
```

```bash
mvn test -Dbrowser=edge
```

---

### 4. Headless Execution

Tests can be executed without opening a visible browser window.

Example:

```bash
mvn test -Dbrowser=firefox -Dheadless=true
```

This is particularly useful for:

* Jenkins
* Docker
* CI/CD pipelines
* Remote execution environments

---

### 5. Data-Driven Testing

The framework supports multiple test-data sources:

* JSON
* Excel
* Cucumber DataTables

This allows test scenarios to remain clean while test data can be maintained independently.

---

### 6. JSON Test Data

Nested JSON test data is parsed using Jackson's `ObjectMapper`.

Example test data:

```text
src/test/resources/testdata/loginData.json
```

Example scenario:

```gherkin
@JsonData @Regression
Scenario: Verify login using credentials loaded from JSON file
  When User logs in using credentials from JSON "testdata/loginData.json" under "validUsers" at index 0
  Then User should be navigated to Account page and verify login status
```

---

### 7. Excel Test Data

Excel-based test data is supported through Apache POI.

Test data can be maintained in:

```text
src/test/resources/testdata/TutorialsNinjaTestData.xlsx
```

Excel processing is encapsulated inside:

```text
ExcelUtils
```

This keeps spreadsheet-specific logic outside the step definitions.

---

### 8. Cucumber DataTable Mapping

Cucumber DataTables can be converted directly into strongly typed Java objects using custom `@DataTableType` configuration.

Example:

```gherkin
@DataTables @Regression
Scenario: Verify registering with complete dataset using Cucumber DataTable
  When User registers with the following user profile:
    | firstName | lastName | telephone  | password | subscribeNewsletter |
    | Mahesh    | Holkar   | 9876543210 | Test@123 | yes                 |
  Then Account success page should display "Your Account Has Been Created!"
```

The DataTable can be mapped to a domain object such as:

```java
UserProfile
```

---

### 9. Custom Cucumber Parameter Types

The framework supports custom Cucumber parameter types for domain-specific input validation.

For example:

```java
@ParameterType("...")
public String emailAddress(String email) {
    return email;
}
```

This allows scenarios to use meaningful custom parameters while keeping validation and conversion logic centralized.

---

### 10. PicoContainer Dependency Injection

Cucumber PicoContainer provides constructor-based dependency injection.

A shared:

```text
TestContext
```

is used to maintain scenario state and facilitate communication between different step-definition classes.

This avoids:

* Static shared state
* Tight coupling
* Manual object creation
* Unnecessary global variables

---

### 11. Automatic Retry Mechanism

Failed tests can be automatically retried using TestNG:

```text
IRetryAnalyzer
IAnnotationTransformer
```

The maximum retry count can be configured through:

```properties
maxRetryCount=2
```

This provides resilience against transient failures.

> Retry should be used as a failure-mitigation mechanism and should not replace investigation of genuine application or automation defects.

---

### 12. Automated Failure Evidence

The framework automatically captures screenshots when scenarios or steps fail.

Cucumber hooks include:

```text
@After
@AfterStep
```

Failure screenshots are attached to:

* Cucumber results
* ExtentReports

This makes debugging failed scenarios significantly easier.

---

### 13. Cross-Environment Configuration

Configuration values can be supplied through multiple sources.

The framework supports:

1. Maven/System properties
2. OS environment variables
3. Properties files

This enables the same test suite to run against different environments without modifying source code.

Example:

```bash
mvn test -Durl=https://example.com -Dbrowser=chrome
```

---

## 🏗 Framework Architecture

The framework follows a layered architecture:

```text
                    ┌─────────────────────────┐
                    │     Cucumber Features   │
                    │       .feature files    │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │     Step Definitions    │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │       TestContext       │
                    │      PicoContainer      │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │       Page Objects      │
                    │   Login / Register etc. │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │       BasePage          │
                    │ Waits / Common Actions  │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │      DriverManager      │
                    │ ThreadLocal<WebDriver>  │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │ Selenium WebDriver      │
                    │ Chrome / Firefox / Edge │
                    └─────────────────────────┘
```

---

## 📁 Project Structure

```text
Selenium-BDD-Automation-Framework/
│
├── src/
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── tutorialsninja/
│       │           └── qa/
│       │               │
│       │               ├── base/
│       │               │   └── BaseTest.java
│       │               │
│       │               ├── context/
│       │               │   └── TestContext.java
│       │               │
│       │               ├── drivers/
│       │               │   └── DriverManager.java
│       │               │
│       │               ├── hooks/
│       │               │   └── Hooks.java
│       │               │
│       │               ├── listeners/
│       │               │   ├── AnnotationTransformer.java
│       │               │   └── RetryAnalyzer.java
│       │               │
│       │               ├── pages/
│       │               │   ├── BasePage.java
│       │               │   ├── LoginPage.java
│       │               │   ├── RegisterPage.java
│       │               │   ├── SearchPage.java
│       │               │   └── AccountPage.java
│       │               │
│       │               ├── runners/
│       │               │   └── TestNGCucumberRunner.java
│       │               │
│       │               ├── stepdefinitions/
│       │               │   ├── LoginSteps.java
│       │               │   ├── RegisterSteps.java
│       │               │   ├── SearchSteps.java
│       │               │   ├── DataTableTypeConfig.java
│       │               │   └── ParameterTypes.java
│       │               │
│       │               └── utils/
│       │                   ├── ConfigReader.java
│       │                   ├── DataUtils.java
│       │                   ├── ExcelUtils.java
│       │                   ├── JsonUtils.java
│       │                   └── ExtentReportManager.java
│       │
│       └── resources/
│           │
│           ├── config/
│           │   ├── config.properties
│           │   └── testdata.properties
│           │
│           ├── features/
│           │   ├── Login.feature
│           │   ├── Register.feature
│           │   └── Search.feature
│           │
│           ├── testdata/
│           │   ├── TutorialsNinjaTestData.xlsx
│           │   └── loginData.json
│           │
│           ├── extent.properties
│           ├── log4j2.xml
│           └── testng.xml
│
├── reports/
│   └── ExtentCucumberReport.html
│
├── logs/
│   └── automation.log
│
├── target/
│   ├── cucumber-reports/
│   └── surefire-reports/
│
├── Jenkinsfile
├── pom.xml
└── README.md
```

---

## ⚙️ Configuration

Default configuration is maintained in:

```text
src/test/resources/config/config.properties
```

Example:

```properties
url=https://tutorialsninja.com/demo/
browser=chrome
headless=false
implicitWait=10
explicitWait=10
pageLoadTimeout=15
maxRetryCount=2
```

### Configuration Parameters

| Property          | Description                  | Default                            |
| ----------------- | ---------------------------- | ---------------------------------- |
| `url`             | Application under test       | `https://tutorialsninja.com/demo/` |
| `browser`         | Target browser               | `chrome`                           |
| `headless`        | Run browser in headless mode | `false`                            |
| `implicitWait`    | Implicit wait in seconds     | `10`                               |
| `explicitWait`    | Explicit wait in seconds     | `10`                               |
| `pageLoadTimeout` | Page load timeout in seconds | `15`                               |
| `maxRetryCount`   | Maximum retry attempts       | `2`                                |

---

## 🔧 Configuration Override

System properties can override values from the properties file.

### Chrome

```bash
mvn test -Dbrowser=chrome
```

### Firefox

```bash
mvn test -Dbrowser=firefox
```

### Edge

```bash
mvn test -Dbrowser=edge
```

### Headless Chrome

```bash
mvn test -Dbrowser=chrome -Dheadless=true
```

### Custom Application URL

```bash
mvn test -Durl=https://tutorialsninja.com/demo/
```

---

## 🧪 Test Data

The framework supports three primary data-driven mechanisms.

### JSON

```text
src/test/resources/testdata/loginData.json
```

### Excel

```text
src/test/resources/testdata/TutorialsNinjaTestData.xlsx
```

### Cucumber DataTables

Scenario-specific data can be supplied directly inside feature files.

This separation allows test logic and test data to evolve independently.

---

## 🥒 Cucumber Tags

Tests can be organized using Cucumber tags.

Examples:

```gherkin
@Smoke
@Regression
@JsonData
@DataTables
```

Tags can also be combined using Cucumber tag expressions.

Example:

```bash
mvn test -Dcucumber.filter.tags="@Regression and not @Smoke"
```

---

## ▶️ Running Tests

### Run the Complete Test Suite

```bash
mvn clean test
```

---

### Run Smoke Tests

```bash
mvn test -Dcucumber.filter.tags="@Smoke"
```

---

### Run Regression Tests

```bash
mvn test -Dcucumber.filter.tags="@Regression"
```

---

### Run Regression Excluding Smoke

```bash
mvn test -Dcucumber.filter.tags="@Regression and not @Smoke"
```

---

### Run JSON/DataTable Scenarios

```bash
mvn test -Dcucumber.filter.tags="@JsonData or @DataTables"
```

---

### Run on Firefox

```bash
mvn test -Dbrowser=firefox
```

---

### Run on Edge

```bash
mvn test -Dbrowser=edge
```

---

### Run Headless Firefox

```bash
mvn test -Dbrowser=firefox -Dheadless=true
```

---

### Run Headless Edge with Smoke Tests

```bash
mvn test -Dbrowser=edge -Dheadless=true -Dcucumber.filter.tags="@Smoke"
```

---

## 🧵 Parallel Execution

Parallel execution is configured through:

```text
src/test/resources/testng.xml
```

Example:

```xml
<suite
    name="Tutorials Ninja BDD Test Suite"
    data-provider-thread-count="3"
    verbose="1">
```

The framework uses:

```java
ThreadLocal<WebDriver>
```

to isolate browser instances between parallel execution threads.

Conceptually:

```text
Thread 1 ──► Chrome Driver ──► Scenario A
Thread 2 ──► Firefox Driver ─► Scenario B
Thread 3 ──► Edge Driver ────► Scenario C
```

This prevents WebDriver instances from being accidentally shared between concurrent scenarios.

---

## 🔄 Retry Mechanism

Failed executions can be retried using TestNG's:

```text
IRetryAnalyzer
IAnnotationTransformer
```

The retry count is configurable:

```properties
maxRetryCount=2
```

For example:

```text
Initial execution
      │
      ▼
   Failure
      │
      ▼
   Retry #1
      │
      ▼
   Failure
      │
      ▼
   Retry #2
```

The retry mechanism is intended primarily for transient failures such as temporary browser or infrastructure instability.

---

## 📸 Failure Evidence

Failure screenshots are automatically captured through Cucumber hooks.

The framework uses:

```java
@After
@AfterStep
```

to detect failures and attach screenshots to the execution results.

Failure evidence can be consumed through:

* ExtentReports
* Cucumber reports

This provides visual context when investigating failed scenarios.

---

## 📊 Reports and Logs

### ExtentReports

The primary HTML report is generated at:

```text
reports/ExtentCucumberReport.html
```

The report provides:

* Feature-level execution details
* Scenario results
* Step-level results
* Execution status
* Failure information
* Embedded screenshots

---

### Cucumber HTML Report

```text
target/cucumber-reports/cucumber.html
```

---

### TestNG Reports

```text
target/surefire-reports/index.html
```

and:

```text
target/surefire-reports/emailable-report.html
```

---

### Execution Logs

Log4j2 execution logs are stored at:

```text
logs/automation.log
```

Rolling file configuration is maintained in:

```text
src/test/resources/log4j2.xml
```

---

## 📈 Reporting Flow

```text
Test Execution
      │
      ├──────────────► Cucumber Results
      │
      ├──────────────► TestNG Results
      │
      ├──────────────► ExtentReports
      │
      └──────────────► Log4j2 Logs
                          │
                          ▼
                  automation.log
```

---

## 🔄 Jenkins CI/CD

The framework includes a declarative:

```text
Jenkinsfile
```

for CI/CD integration.

Jenkins can provide execution parameters such as:

```text
browser
headless
cucumber.filter.tags
url
```

Example Maven execution:

```bash
mvn clean test \
  -Dbrowser=chrome \
  -Dheadless=true \
  -Dcucumber.filter.tags="@Smoke"
```

This makes the framework suitable for automated regression execution in CI environments.

---

## 🧩 Core Components

### `DriverManager`

Responsible for:

* WebDriver creation
* Browser selection
* Thread-local driver management
* Driver cleanup

---

### `BasePage`

Provides reusable Selenium functionality such as:

* Explicit waits
* Element interactions
* Click operations
* Text entry
* Element visibility checks
* Common browser actions

---

### `TestContext`

Provides shared scenario-level state using PicoContainer.

---

### `ConfigReader`

Loads configuration from:

* Properties files
* System properties
* Environment variables

---

### `JsonUtils`

Provides JSON parsing through Jackson.

---

### `ExcelUtils`

Provides Excel workbook and worksheet processing through Apache POI.

---

### `ExtentReportManager`

Manages ExtentReports configuration and report lifecycle.

---

### `Hooks`

Handles Cucumber lifecycle operations including:

* Setup
* Teardown
* Failure detection
* Screenshot capture
* Report integration

---

## 🧱 Design Principles

The framework follows several key automation engineering principles.

### Separation of Concerns

Test scenarios, page interactions, configuration, test data, reporting, and driver management are separated into dedicated layers.

### Reusability

Common Selenium operations are centralized in `BasePage`.

### Maintainability

UI changes can generally be isolated to their respective Page Object classes.

### Scalability

Thread-safe WebDriver management allows additional parallel execution capacity.

### Configuration Flexibility

Environment-specific settings can be overridden without modifying source code.

### Test Data Independence

JSON, Excel, and DataTable-based test data are separated from core automation logic.

### Observability

Logs, reports, and screenshots provide execution evidence for troubleshooting.

---

## 📋 Prerequisites

Before running the framework, ensure the following are installed:

### Java

Java 21 or later:

```bash
java -version
```

Expected:

```text
java version "21"
```

### Maven

Verify Maven installation:

```bash
mvn -version
```

### Browser

At least one supported browser should be installed:

* Chrome
* Firefox
* Edge

Selenium Manager is used by Selenium WebDriver to assist with browser driver management.

---

## 📥 Clone the Repository

```bash
git clone <repository-url>
cd Selenium-BDD-Automation-Framework
```

---

## 📦 Install Dependencies

Maven automatically downloads the required dependencies defined in:

```text
pom.xml
```

Run:

```bash
mvn clean install
```

---

## 🚀 Quick Start

The simplest way to execute the complete suite is:

```bash
mvn clean test
```

For a CI-friendly headless smoke execution:

```bash
mvn clean test -Dbrowser=chrome -Dheadless=true -Dcucumber.filter.tags="@Smoke"
```

After execution, review:

```text
reports/ExtentCucumberReport.html
```

for the detailed ExtentReports execution results.

---

## 🔍 Example Execution Matrix

| Execution                  | Command                                                        |
| -------------------------- | -------------------------------------------------------------- |
| Full suite                 | `mvn clean test`                                               |
| Smoke                      | `mvn test -Dcucumber.filter.tags="@Smoke"`                     |
| Regression                 | `mvn test -Dcucumber.filter.tags="@Regression"`                |
| Firefox                    | `mvn test -Dbrowser=firefox`                                   |
| Edge                       | `mvn test -Dbrowser=edge`                                      |
| Headless Chrome            | `mvn test -Dbrowser=chrome -Dheadless=true`                    |
| Headless Firefox           | `mvn test -Dbrowser=firefox -Dheadless=true`                   |
| JSON/DataTables            | `mvn test -Dcucumber.filter.tags="@JsonData or @DataTables"`   |
| Regression excluding Smoke | `mvn test -Dcucumber.filter.tags="@Regression and not @Smoke"` |

---

## 📌 Framework Highlights

```text
✓ Java 21
✓ Selenium WebDriver 4.49.0
✓ Cucumber BDD 7.34.9
✓ TestNG 7.10.2
✓ Page Object Model
✓ Thread-safe parallel execution
✓ Chrome / Firefox / Edge
✓ Headless execution
✓ JSON data-driven testing
✓ Excel data-driven testing
✓ Cucumber DataTables
✓ Custom ParameterTypes
✓ PicoContainer dependency injection
✓ Automatic retry mechanism
✓ Automatic failure screenshots
✓ ExtentReports
✓ Cucumber HTML reports
✓ TestNG reports
✓ Log4j2 logging
✓ Jenkins CI/CD integration
✓ Environment-aware configuration
```

---

## 🤝 Contribution

Contributions that improve framework stability, maintainability, reporting, test coverage, or CI/CD capabilities are welcome.

When contributing:

1. Create a feature branch.
2. Implement the required changes.
3. Add or update automated tests where appropriate.
4. Run the complete test suite.
5. Verify generated reports.
6. Submit a pull request.

---

## 📄 License

Add the appropriate project license here, for example:

```text
This project is licensed under the MIT License.
```

Update this section according to the license used by the repository.

---

## 👨‍💻 Author

**Mahesh Holkar**

Selenium BDD Automation Framework
Java | Selenium | Cucumber | TestNG | Maven | Jenkins

---

## ⭐ Project Summary

This framework provides an enterprise-oriented foundation for **BDD-based Selenium automation**, combining clean Page Object architecture, thread-safe parallel execution, multiple data-driven approaches, dependency injection, automatic failure evidence, reporting, logging, and CI/CD support.

It is structured to make automated tests **readable, reusable, maintainable, scalable, and suitable for continuous integration environments**.
