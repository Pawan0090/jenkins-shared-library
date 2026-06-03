def call(Map config) {
    pipeline {
        // We use the master node since it has your laptop's Docker socket attached
        agent { label 'built-in' } 
        
        stages {
            stage('Build Docker Image') {
                steps {
                    echo "Building image for ${config.appName}..."
                    // 'checkout scm' automatically pulls the code from the triggered branch
                    checkout scm 
                    sh "docker build -t ${config.appName}:${env.BUILD_ID} -t ${config.appName}:latest ."
                }
            }
            stage('Deploy to Local Server') {
                steps {
                    echo "Deploying to port ${config.hostPort}..."
                    // 1. Force remove the old container if it exists (Zero Downtime simulation)
                    sh "docker rm -f ${config.appName} || true"
                    
                    // 2. Spin up the new container mapping it to your laptop's port
                    sh "docker run -d -p ${config.hostPort}:80 --name ${config.appName} ${config.appName}:latest"
                }
            }
        }
    }
}
