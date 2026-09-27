pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
        jdk 'JAVA_HOME'
    }

    parameters {
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Select target browser for execution'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browser in headless mode (recommended for CI)'
        )
        string(
            name: 'CUCUMBER_TAGS',
            defaultValue: '@Regression',
            description: 'Cucumber tag expression (e.g. @Smoke, @Regression, "@Smoke or @Regression")'
        )
    }

    options {
        timeout(time: 60, unit: 'MINUTES')
        ansiColor('xterm')
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout Code') {
            steps {
                cleanWs()
                checkout scm
            }
        }

        stage('Compile Project') {
            steps {
                bat 'mvn clean test-compile'
            }
        }

        stage('Execute BDD Tests') {
            steps {
                script {
                    // Normalize tags and trim whitespace
                    def tags = params.CUCUMBER_TAGS ? params.CUCUMBER_TAGS.trim() : ''
                    def tagOption = tags.isEmpty() ? '' : "-Dcucumber.filter.tags=\"${tags}\""

                    // Execute maven on Windows batch
                    bat """
                        mvn test ^
                        -Dbrowser=${params.BROWSER} ^
                        -Dheadless=${params.HEADLESS} ^
                        ${tagOption}
                    """
                }
            }
        }
    }

    post {
        always {
            // 1. Publish Extent BDD Cucumber Report
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'ExtentCucumberReport.html',
                reportName: 'Extent BDD Report'
            ])

            // 2. Publish Standard TestNG Emailable Report
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/surefire-reports',
                reportFiles: 'emailable-report.html',
                reportName: 'TestNG Emailable Report'
            ])

            // 3. Publish Full TestNG Index Suite Report
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/surefire-reports',
                reportFiles: 'index.html',
                reportName: 'TestNG Suite Report'
            ])

            // 4. Archive Artifacts
            archiveArtifacts artifacts: 'reports/**, target/surefire-reports/**', allowEmptyArchive: true

            // 5. Publish JUnit test results for health metrics & trend graphs
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
        }
        failure {
            echo "Pipeline failed. Check build logs, screenshots, and ExtentReports under 'reports/'."
        }
    }
}