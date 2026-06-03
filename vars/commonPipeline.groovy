def call(Map config) {
    pipeline {
        agent { label 'built-in' } 
        
        stages {
            stage('Build Docker Image') {
                steps {
                    echo "Building image for ${config.appName}..."
                    checkout scm 
                    sh "docker build -t ${config.appName}:${env.BUILD_ID} -t ${config.appName}:latest ."
                }
            }
            
            // NEW: The Parallel Testing Block
            stage('Automated Quality Checks') {
                parallel {
                    stage('Unit Tests') {
                        steps {
                            echo "Starting fast unit tests..."
                            // Simulating a test that takes 5 seconds
                            sh 'sleep 5' 
                            echo "Unit tests passed!"
                        }
                    }
                    stage('Security Vulnerability Scan') {
                        steps {
                            echo "Starting heavy security scan..."
                            // Simulating a scan that takes 8 seconds
                            sh 'sleep 8' 
                            echo "No vulnerabilities found!"
                        }
                    }
                    stage('Code Linting') {
                        steps {
                            echo "Checking code formatting..."
                            // Simulating a quick format check
                            sh 'sleep 3' 
                            echo "Code is perfectly formatted!"
                        }
                    }
                }
            }
            
            stage('Deploy to Local Server') {
                steps {
                    echo "Tests passed! Deploying to port ${config.hostPort}..."
                    sh "docker rm -f ${config.appName} || true"
                    sh "docker run -d -p ${config.hostPort}:80 --name ${config.appName} ${config.appName}:latest"
                }
            }
        }
    }
}
