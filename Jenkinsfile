pipeline {
    agent any

    environment {
        DOCKER_HUB_REPO = 'ayedyoussef777/foyer-app'  // Remplacez par votre repository Docker Hub
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials' // L'ID des identifiants Docker Hub dans Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                // Récupérer le projet depuis GitHub
                git branch: 'master', url: 'https://youssefayed213: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@github.com/youssefayed213/DevOps'
            }
        }

        stage('Clean') {
            steps {
                // Nettoyer le projet Maven
                sh 'mvn clean'
            }
        }

        stage('Build without Tests') {
            steps {
                // Compiler le projet sans exécuter les tests
                sh 'mvn package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                // Construire l'image Docker
                script {
                    sh 'docker build -t ${DOCKER_HUB_REPO}:${BUILD_NUMBER} .'
                }
            }
        }

        stage('Docker Push') {
            steps {
                // Pousser l'image vers Docker Hub
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
                // Tirer l'image depuis Docker Hub pour déploiement
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
