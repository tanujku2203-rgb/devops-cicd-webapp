
pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'tanuj2203/devops-cicd-webapp'
        EC2_HOST = '52.87.199.96'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} -t ${DOCKER_IMAGE}:latest .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKERHUB_USERNAME',
                    passwordVariable: 'DOCKERHUB_TOKEN'
                )]) {
                    sh '''
                        echo "$DOCKERHUB_TOKEN" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin
                        docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}
                        docker push ${DOCKER_IMAGE}:latest
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy to AWS EC2') {
            steps {
                withCredentials([sshUserPrivateKey(
                    credentialsId: 'ec2-ssh-key',
                    keyFileVariable: 'SSH_KEY',
                    usernameVariable: 'SSH_USER'
                )]) {
                    sh '''
                        ssh -i "$SSH_KEY" -o StrictHostKeyChecking=no $SSH_USER@$EC2_HOST '
                            sudo docker pull ${DOCKER_IMAGE}:latest &&
                            (sudo docker rm -f devops-webapp || true) &&
                            sudo docker run -d --restart unless-stopped \
                            --name devops-webapp -p 80:8090 ${DOCKER_IMAGE}:latest
                        '
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Build, Docker push and EC2 deployment completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the console output.'
        }
    }
}

