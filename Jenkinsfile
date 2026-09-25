pipeline {
    agent any

    tools {
        maven 'Maven-3.9.9'   // Must match the Maven name configured in Jenkins Tools
        jdk 'JDK-21'          // Must match the JDK 21 name configured in Jenkins Tools
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Target Browser')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
        string(name: 'CUCUMBER_TAGS', defaultValue: '@Regression', description: 'Cucumber Tag Expression to execute')
        string(name: 'THREAD_COUNT', defaultValue: '2', description: 'Parallel execution thread count')
    }

    stages {
        stage('Checkout Source') {
            steps {
                git branch: 'main', url: 'https://github.com/MaheshH91/Selenium-BDD-Automation-Framework.git'
            }
        }

        stage('Compile & Validate') {
            steps {
                bat 'mvn clean test-compile'
            }
        }

        stage('Execute BDD Test Suite') {
            steps {
                // For Windows Jenkins node use 'bat', for Linux node use 'sh'
                bat """
                    mvn test ^
                    -Dbrowser=${params.BROWSER} ^
                    -Dheadless=${params.HEADLESS} ^
                    -Dcucumber.filter.tags="${params.CUCUMBER_TAGS}"
                """
            }
        }
    }

    post {
        always {
            // Archive Extent Cucumber HTML Report
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'ExtentCucumberReport.html',
                reportName: 'Extent BDD Execution Report'
            ])

            // Archive TestNG XML and Surefire summaries
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
        }
        failure {
            echo "Pipeline failed: Tests encountered failures or errors."
        }
        success {
            echo "Pipeline passed: All automated BDD scenarios executed cleanly."
        }
    }
}