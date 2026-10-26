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
            // 1. Record JUnit test results first so test tokens are populated for emailext
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true

            // 2. Archive Artifacts
            archiveArtifacts artifacts: 'reports/**, logs/**, target/surefire-reports/**', allowEmptyArchive: true

            // 3. Email Notification with Embedded Test Execution Dashboard
            emailext(
                to: 'holkarmahesh@outlook.com',
                subject: "Test Execution Summary - ${currentBuild.currentResult}: Job '${env.JOB_NAME}' (Build #${env.BUILD_NUMBER})",
                body: '''
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <style>
                            body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; font-size: 14px; color: #333; margin: 0; padding: 20px; }
                            .card { border: 1px solid #dadce0; border-radius: 8px; max-width: 650px; padding: 20px; background-color: #ffffff; }
                            .banner { padding: 12px 18px; border-radius: 6px; font-weight: bold; font-size: 16px; margin-bottom: 20px; }
                            .banner-success { background-color: #e6f4ea; color: #137333; border: 1px solid #ceead6; }
                            .banner-failure { background-color: #fce8e6; color: #c5221f; border: 1px solid #fad2cf; }
                            .banner-unstable { background-color: #feefc3; color: #b06000; border: 1px solid #fdd663; }
                            .metric-table { width: 100%; border-collapse: separate; border-spacing: 8px; margin-bottom: 20px; }
                            .metric-box { padding: 14px 10px; border-radius: 6px; text-align: center; }
                            .metric-total { background-color: #f1f3f4; border: 1px solid #dadce0; }
                            .metric-passed { background-color: #e6f4ea; border: 1px solid #ceead6; color: #137333; }
                            .metric-failed { background-color: #fce8e6; border: 1px solid #fad2cf; color: #c5221f; }
                            .metric-skipped { background-color: #feefc3; border: 1px solid #fdd663; color: #b06000; }
                            .val { font-size: 24px; font-weight: bold; margin-bottom: 4px; }
                            .lbl { font-size: 11px; text-transform: uppercase; letter-spacing: 0.5px; font-weight: 600; }
                            .details-table { width: 100%; border-collapse: collapse; margin-top: 15px; font-size: 13px; }
                            .details-table th, .details-table td { border: 1px solid #e1e4e8; padding: 10px 14px; text-align: left; }
                            .details-table th { background-color: #f8f9fa; width: 35%; font-weight: 600; }
                        </style>
                    </head>
                    <body>
                        <div class="card">
                            <div class="banner ${BUILD_STATUS == 'SUCCESS' ? 'banner-success' : (BUILD_STATUS == 'UNSTABLE' ? 'banner-unstable' : 'banner-failure')}">
                                Build Status: ${BUILD_STATUS}
                            </div>

                            <table class="metric-table">
                                <tr>
                                    <td class="metric-box metric-total">
                                        <div class="val">${TEST_COUNTS, var="total"}</div>
                                        <div class="lbl">Total Tests</div>
                                    </td>
                                    <td class="metric-box metric-passed">
                                        <div class="val">${TEST_COUNTS, var="pass"}</div>
                                        <div class="lbl">Passed</div>
                                    </td>
                                    <td class="metric-box metric-failed">
                                        <div class="val">${TEST_COUNTS, var="fail"}</div>
                                        <div class="lbl">Failed</div>
                                    </td>
                                    <td class="metric-box metric-skipped">
                                        <div class="val">${TEST_COUNTS, var="skip"}</div>
                                        <div class="lbl">Skipped</div>
                                    </td>
                                </tr>
                            </table>

                            <h3 style="margin: 0 0 10px 0; font-size: 14px; border-bottom: 2px solid #f1f3f4; padding-bottom: 6px;">Execution Metadata</h3>
                            <table class="details-table">
                                <tr><th>Job Name</th><td>${JOB_NAME}</td></tr>
                                <tr><th>Build Number</th><td>#${BUILD_NUMBER}</td></tr>
                                <tr><th>Execution Profile</th><td>${MAVEN_PROFILE}</td></tr>
                                <tr><th>Browser</th><td>${BROWSER} (Headless: ${HEADLESS})</td></tr>
                                <tr><th>Duration</th><td>${BUILD_DURATION}</td></tr>
                            </table>

                            <!-- If tests fail, show the failed scenarios table directly -->
                            ${FAILED_TESTS}

                            <p style="font-size: 12px; color: #5f6368; margin-top: 20px; line-height: 1.4;">
                                The full interactive <b>ExtentCucumberReport.html</b> and standard <b>emailable-report.html</b> files are attached directly to this email.
                            </p>
                        </div>
                    </body>
                    </html>
                ''',
                mimeType: 'text/html',
                attachmentsPattern: 'reports/ExtentCucumberReport.html, target/surefire-reports/emailable-report.html',
                recipientProviders: [developers(), requestor()]
            )
        }
    }
}