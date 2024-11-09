pipeline {
    agent any

    environment {
        DOCKER_HUB_REPO = 'ayedyoussef777/foyer-app' // Your Docker Hub repository
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials' // Jenkins credentials ID for Docker Hub
        SONAR_SERVER = 'SonarQube' // Name of the SonarQube server configured in Jenkins
        SONAR_TOKEN = credentials('jenkins-sonar') // Jenkins credentials ID for SonarQube token
    }

    stages {
        stage('Checkout') {
            steps {
                // Clone the project from GitHub
                git branch: 'master', url: 'https://youssefayed213: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@github.com/youssefayed213/DevOps'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // Run SonarQube analysis
                withSonarQubeEnv('SonarQube') {  // 'SonarQube' is the name of the SonarQube server configured in Jenkins
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=foyeeer -Dsonar.login=$SONAR_TOKEN'
                }
            }
        }

        stage('Clean') {
            steps {
                // Clean the Maven project
                sh 'mvn clean'
            }
        }

        stage('Build without Tests') {
            steps {
                // Build the project without running tests
                sh 'mvn package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                // Build Docker image
                script {
                    sh 'docker build -t ${DOCKER_HUB_REPO}:${BUILD_NUMBER} .'
                }
            }
        }

        stage('Docker Push') {
            steps {
                // Push Docker image to Docker Hub
                script {
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, passwordVariable: 'DOCKER_HUB_PASSWORD', usernameVariable: 'DOCKER_HUB_USERNAME')]) {
                        sh "echo $DOCKER_HUB_PASSWORD | docker login -u $DOCKER_HUB_USERNAME --password-stdin"
                        sh "docker push ${DOCKER_HUB_REPO}:${BUILD_NUMBER}"
                    }
                }
            }
        }

        stage('Pull from Docker Hub') {
            steps {
                // Pull the Docker image from Docker Hub for deployment
                script {
                    sh "docker pull ${DOCKER_HUB_REPO}:${BUILD_NUMBER}"
                }
            }
        }
    }

    post {
        success {
            echo 'Le pipeline de livraison continue a réussi !'
        }
        failure {
            echo 'Le pipeline de livraison continue a échoué.'
        }
    }
}
//d
