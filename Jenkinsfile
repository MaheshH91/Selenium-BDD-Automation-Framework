pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
        jdk 'JAVA_HOME'
    }

    parameters {
        choice(
            name: 'MAVEN_PROFILE',
            choices: ['bdd-suite', 'testng-tests'],
            description: 'Select execution profile: BDD Cucumber Suite or Standalone TestNG Tests'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Target browser for execution'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browser in headless mode (recommended for CI)'
        )
        string(
            name: 'CUCUMBER_TAGS',
            defaultValue: '@Regression',
            description: 'Cucumber tag filter (e.g. @Smoke, @Regression, "@Smoke or @Regression")'
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
                bat 'mvn clean test-compile -DargLine="--enable-native-access=ALL-UNNAMED"'
            }
        }

        stage('Execute Tests') {
            steps {
                script {
                    def tagArg = ''
                    if (params.MAVEN_PROFILE == 'bdd-suite') {
                        def tags = params.CUCUMBER_TAGS ? params.CUCUMBER_TAGS.trim() : ''
                        tagArg = tags.isEmpty() ? '' : "-Dcucumber.filter.tags=\"${tags}\""
                    }

                    bat """
                        mvn test ^
                        -P ${params.MAVEN_PROFILE} ^
                        -Dbrowser=${params.BROWSER} ^
                        -Dheadless=${params.HEADLESS} ^
                        -DargLine="--enable-native-access=ALL-UNNAMED" ^
                        ${tagArg}
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

            // 2. Publish Standard TestNG Emailable Summary Report
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/surefire-reports',
                reportFiles: 'emailable-report.html',
                reportName: 'TestNG Emailable Report'
            ])

            // 3. Publish Complete TestNG Index Suite Report
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/surefire-reports',
                reportFiles: 'index.html',
                reportName: 'TestNG Suite Report'
            ])

            // 4. Archive Artifacts (HTML reports, execution logs, and screenshots)
            archiveArtifacts artifacts: 'reports/**, logs/**, target/surefire-reports/**', allowEmptyArchive: true

            // 5. Publish JUnit test results for trend graphs
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true

            // 6. Email Notification with report attachments & execution metadata
            emailext(
                to: 'holkarmahesh@outlook.com',
                subject: "[Jenkins] ${currentBuild.currentResult}: Job '${env.JOB_NAME}' (Build #${env.BUILD_NUMBER})",
                body: """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <style>
                            body { font-family: Arial, sans-serif; font-size: 14px; color: #333; line-height: 1.6; }
                            .status-box { padding: 12px 16px; border-radius: 4px; font-weight: bold; margin-bottom: 16px; }
                            .SUCCESS { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
                            .FAILURE { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
                            .UNSTABLE { background-color: #fff3cd; color: #856404; border: 1px solid #ffeeba; }
                            table { width: 100%; max-width: 600px; border-collapse: collapse; margin-top: 10px; }
                            th, td { padding: 8px 12px; border: 1px solid #ddd; text-align: left; }
                            th { background-color: #f2f2f2; width: 35%; }
                            a { color: #0066cc; text-decoration: none; }
                            a:hover { text-decoration: underline; }
                        </style>
                    </head>
                    <body>
                        <div class="status-box ${currentBuild.currentResult}">
                            Build Status: ${currentBuild.currentResult}
                        </div>

                        <table>
                            <tr><th>Job Name</th><td>${env.JOB_NAME}</td></tr>
                            <tr><th>Build Number</th><td>#${env.BUILD_NUMBER}</td></tr>
                            <tr><th>Maven Profile</th><td>${params.MAVEN_PROFILE ?: 'bdd-suite'}</td></tr>
                            <tr><th>Browser</th><td>${params.BROWSER} (Headless: ${params.HEADLESS})</td></tr>
                            <tr><th>Cucumber Tags</th><td>${params.CUCUMBER_TAGS ?: 'N/A'}</td></tr>
                            <tr><th>Duration</th><td>${currentBuild.durationString.replace(' and counting', '')}</td></tr>
                        </table>

                        <h3>Execution Artifacts & Reports:</h3>
                        <ul>
                            <li><a href="${env.BUILD_URL}Extent_20BDD_20Report/">View Extent BDD HTML Report</a></li>
                            <li><a href="${env.BUILD_URL}TestNG_20Emailable_20Report/">View TestNG Emailable Report</a></li>
                            <li><a href="${env.BUILD_URL}testReport/">View JUnit Trend & Health Graph</a></li>
                            <li><a href="${env.BUILD_URL}console">View Jenkins Console Output</a></li>
                        </ul>
                        <p><i>Note: The Extent Report and TestNG Emailable summary are also attached to this email.</i></p>
                    </body>
                    </html>
                """,
                mimeType: 'text/html',
                attachmentsPattern: 'reports/ExtentCucumberReport.html, target/surefire-reports/emailable-report.html',
                recipientProviders: [developers(), requestor()]
            )
        }

        success {
            echo "All tests executed and passed under profile: '${params.MAVEN_PROFILE}'"
        }

        failure {
            echo "Build failed. Inspect ExtentReport, TestNG reports, or console logs for details."
        }
    }
}