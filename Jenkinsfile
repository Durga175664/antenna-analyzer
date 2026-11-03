pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // GitHub-la irunthu code pull panrom
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // Maven clean test run aagi unit tests & HTML report-a generate pannum
                sh 'mvn clean test'
            }
        }
    }

    post {
        always {
            // Jenkins dashboard-la HTML Report-a publish panrathu
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target',
                reportFiles: 'antenna-analysis-report.html',
                reportName: 'Antenna Analysis Report',
                includes: '**/*'
            ])
        }
    }
}