pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // Windows OS kaaga 'sh' ku badhula 'bat' use panrom
                bat 'mvn clean test'
            }
        }
    }

    post {
        always {
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