# Selenium BDD Automation Framework

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4.49.0-43B02A?style=for-the-badge&logo=selenium&logoColor=white)
![Cucumber](https://img.shields.io/badge/Cucumber-7.34.9-23D18B?style=for-the-badge&logo=cucumber&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7.10.2-FFB703?style=for-the-badge&logo=testng&logoColor=white)
![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-D24939?style=for-the-badge&logo=jenkins&logoColor=white)
![Browsers](https://img.shields.io/badge/Browsers-Chrome%20%7C%20Firefox%20%7C%20Edge-4285F4?style=for-the-badge&logo=googlechrome&logoColor=white)

An enterprise-grade end-to-end test automation framework for scalable web application testing, built with Java 21, Selenium WebDriver, Cucumber BDD, TestNG, and Maven.

This framework is designed for maintainability, scalability, parallel execution, data-driven testing, reusable Page Objects, automated failure evidence, and CI/CD integration.

The project currently targets the [TutorialsNinja Demo Application](https://tutorialsninja.com/demo/).

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Key Features](#key-features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Parallel Execution](#parallel-execution)
- [Retry Mechanism](#retry-mechanism)
- [Reports and Logs](#reports-and-logs)
- [CI/CD with Jenkins](#cicd-with-jenkins)
- [Contributing](#contributing)
- [License](#license)
- [Author](#author)

## Overview

This framework provides a structured approach to automating browser-based web applications using Behavior-Driven Development (BDD).

It combines:

- Selenium WebDriver for browser automation
- Cucumber for readable Gherkin scenarios
- TestNG for test execution and parallelization
- PicoContainer for dependency injection and state sharing
- Page Object Model (POM) for UI abstraction
- JSON and Excel for external test data
- Cucumber DataTables for scenario-level data
- ExtentReports for rich reporting
- Log4j2 for logging
- Jenkins for CI/CD execution

The architecture is designed to support multiple browsers, environment-specific configuration, reusable test components, and enterprise-level reporting.

## Tech Stack

| Technology | Version / Details |
| --- | --- |
| Java | 21 |
| Selenium WebDriver | 4.49.0 |
| Cucumber Java | 7.34.9 |
| Cucumber TestNG | 7.34.9 |
| TestNG | 7.10.2 |
| PicoContainer | Dependency Injection |
| Apache Maven | Build & dependency management |
| Apache POI | 5.2.5 |
| Jackson Databind | 2.17.0 |
| ExtentReports Adapter | 1.14.0 |
| Log4j2 | 2.20.0 |
| CI/CD | Jenkins |
| Browsers | Chrome, Firefox, Edge |

## Key Features

- Page Object Model (POM) for maintainable UI automation
- Thread-safe parallel execution using `ThreadLocal<WebDriver>`
- Multi-browser support: Chrome, Firefox, Edge
- Headless execution support for CI environments
- JSON, Excel, and Cucumber DataTable-driven testing
- Cucumber custom parameter types and type-safe data mapping
- PicoContainer-based scenario state management
- Automatic retry support via TestNG `IRetryAnalyzer`
- Automated failure screenshots and report attachment
- ExtentReports, Cucumber HTML, and TestNG reporting
- Environment-aware configuration using system properties, env vars, and properties files

## Project Structure

```text
Selenium-BDD-Automation-Framework/
├── src/
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── tutorialsninja/
│       │           └── qa/
│       │               ├── base/
│       │               │   └── BaseTest.java
│       │               ├── context/
│       │               │   └── TestContext.java
│       │               ├── drivers/
│       │               │   └── DriverManager.java
│       │               ├── hooks/
│       │               │   └── Hooks.java
│       │               ├── listeners/
│       │               │   ├── AnnotationTransformer.java
│       │               │   └── RetryAnalyzer.java
│       │               ├── pages/
│       │               │   ├── BasePage.java
│       │               │   ├── LoginPage.java
│       │               │   ├── RegisterPage.java
│       │               │   ├── SearchPage.java
│       │               │   └── AccountPage.java
│       │               ├── runners/
│       │               │   └── TestNGCucumberRunner.java
│       │               ├── stepdefinitions/
│       │               │   ├── LoginSteps.java
│       │               │   ├── RegisterSteps.java
│       │               │   ├── SearchSteps.java
│       │               │   ├── DataTableTypeConfig.java
│       │               │   └── ParameterTypes.java
│       │               └── utils/
│       │                   ├── ConfigReader.java
│       │                   ├── DataUtils.java
│       │                   ├── ExcelUtils.java
│       │                   ├── JsonUtils.java
│       │                   └── ExtentReportManager.java
│       └── resources/
│           ├── config/
│           │   ├── config.properties
│           │   └── testdata.properties
│           ├── features/
│           │   ├── Login.feature
│           │   ├── Register.feature
│           │   └── Search.feature
│           ├── testdata/
│           │   ├── TutorialsNinjaTestData.xlsx
│           │   └── loginData.json
│           ├── extent.properties
│           ├── log4j2.xml
│           └── testng.xml
├── reports/
│   └── ExtentCucumberReport.html
├── logs/
│   └── automation.log
├── target/
│   ├── cucumber-reports/
│   └── surefire-reports/
├── Jenkinsfile
├── pom.xml
├── .gitignore
├── README.md
└── LICENSE
```

## Prerequisites

Before running the framework, ensure the following are installed:

### Java

```bash
java -version
```

Required version: Java 21 or later.

### Maven

```bash
mvn -version
```

### Browsers

Install at least one supported browser:

- Chrome
- Firefox
- Edge

Selenium Manager is used to manage browser drivers automatically.

## Getting Started

### Clone the repository

```bash
git clone <repository-url>
cd Selenium-BDD-Automation-Framework
```

### Install dependencies

```bash
mvn clean install
```

## Configuration

Default values are maintained in:

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

### Configuration overrides

You can override configuration values via system properties:

```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
mvn test -Durl=https://tutorialsninja.com/demo/
mvn test -Dbrowser=chrome -Dheadless=true
```

## Running Tests

### Run the complete suite

```bash
mvn clean test
```

### Run smoke tests

```bash
mvn test -Dcucumber.filter.tags="@Smoke"
```

### Run regression tests

```bash
mvn test -Dcucumber.filter.tags="@Regression"
```

### Run tests excluding smoke

```bash
mvn test -Dcucumber.filter.tags="@Regression and not @Smoke"
```

### Run JSON/DataTable scenarios

```bash
mvn test -Dcucumber.filter.tags="@JsonData or @DataTables"
```

### Run on Firefox

```bash
mvn test -Dbrowser=firefox
```

### Run on Edge

```bash
mvn test -Dbrowser=edge
```

### Run headless Firefox

```bash
mvn test -Dbrowser=firefox -Dheadless=true
```

## Parallel Execution

Parallel execution is configured in:

```text
src/test/resources/testng.xml
```

The framework uses `ThreadLocal<WebDriver>` to isolate browser sessions between threads and prevent accidental sharing across scenarios.

Example configuration:

```xml
<suite
    name="Tutorials Ninja BDD Test Suite"
    data-provider-thread-count="3"
    verbose="1">
```

## Retry Mechanism

Failed tests can be retried using TestNG:

- `IRetryAnalyzer`
- `IAnnotationTransformer`

The retry count is configurable via:

```properties
maxRetryCount=2
```

This is primarily intended for transient failures such as infrastructure instability or temporary browser issues.

## Reports and Logs

### ExtentReports

```text
reports/ExtentCucumberReport.html
```

### Cucumber HTML report

```text
target/cucumber-reports/cucumber.html
```

### TestNG reports

```text
target/surefire-reports/index.html
target/surefire-reports/emailable-report.html
```

### Execution logs

```text
logs/automation.log
```

## CI/CD with Jenkins

The project includes a declarative `Jenkinsfile` for pipeline-based execution.

Sample Maven command:

```bash
mvn clean test \
  -Dbrowser=chrome \
  -Dheadless=true \
  -Dcucumber.filter.tags="@Smoke"
```

This makes the framework suitable for automated regression runs in CI/CD pipelines.

## Contributing

Contributions are welcome to improve:

- framework stability
- maintainability
- reporting
- test coverage
- CI/CD integration

Suggested process:

1. Create a feature branch
2. Make the required changes
3. Add or update automated tests where needed
4. Run the test suite
5. Verify generated reports
6. Open a pull request

## License

Add the appropriate project license here. For example:

```text
This project is licensed under the MIT License.
```

Update this section according to the license used by the repository.

## Author

**Mahesh Holkar**

Software QA Engineer | Selenium | Cucumber | TestNG | Maven | Jenkins

## Author & Profiles

- **GitHub Profile:** [MaheshH91](https://github.com/MaheshH91)
- **LinkedIn Profile:** [Mahesh Holkar](https://www.linkedin.com/in/maheshholkarqaengineer/)

---

This framework provides a scalable foundation for BDD-based Selenium automation with clean Page Object architecture, thread-safe execution, multiple data-driven approaches, and CI-friendly execution workflows.
