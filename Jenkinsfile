pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
        jdk 'JAVA_HOME'
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Target Browser')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
        string(name: 'CUCUMBER_TAGS', defaultValue: '@Regression', description: 'Cucumber Tag Expression to execute')
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
          // 1. Publish Extent BDD Cucumber Report
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'ExtentCucumberReport.html',
                reportName: 'Extent BDD Report'
            ])

            // 2. Publish Standard TestNG HTML Report
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/surefire-reports',
                reportFiles: 'emailable-report.html',
                reportName: 'TestNG Emailable Report'
            ])

            // 3. Publish TestNG Full Index Report
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/surefire-reports',
                reportFiles: 'index.html',
                reportName: 'TestNG Suite Report'
            ])

            // 4. Archive Artifacts for direct download
            archiveArtifacts artifacts: 'reports/**, target/surefire-reports/**', allowEmptyArchive: true

            // 5. TestNG JUnit Trend Graph
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
        }
    }
}