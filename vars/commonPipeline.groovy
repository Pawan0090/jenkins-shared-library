def call(Map config = [:]) {
    pipeline {
        agent any
        stages {
            stage('Initialize') {
                steps {
                    echo "Initializing build for application: ${config.appName}"
                    echo "Deploying to environment: ${config.environment}"
                }
            }
            stage('Build & Test Artifact') {
                steps {
                    echo "Simulating application build step..."
                    sh 'echo "Artifact Compiled successfully."'
                }
            }
            stage('Quality Gate') {
                when {
                    expression { config.runSonarQube == true }
                }
                steps {
                    echo "Running code analysis scans..."
                }
            }
        }
    }
}
