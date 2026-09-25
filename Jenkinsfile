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
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'ExtentCucumberReport.html',
                reportName: 'Extent BDD Execution Report'
            ])

            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
        }
    }
}