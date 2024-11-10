pipeline {
    agent any

    environment {
        DOCKER_HUB_REPO = 'ayedyoussef777/foyer-app'
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
        SONAR_SERVER = 'SonarQube'
        SONAR_TOKEN = credentials('jenkins-sonar')
        NEXUS_URL = 'http://localhost:8081/repository/maven-snapshots/'
        NEXUS_CREDENTIALS_ID = 'jenkins-nexus'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://youssefayed213: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@github.com/youssefayed213/DevOps'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=foyer22 -Dsonar.login=$SONAR_TOKEN -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml -X'
                }
            }
        }
        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }
        stage('Build without Tests') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    sh 'docker-compose build --no-cache'
                }
            }
        }
        stage('Docker Push') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, passwordVariable: 'DOCKER_HUB_PASSWORD', usernameVariable: 'DOCKER_HUB_USERNAME')]) {
                        sh "echo $DOCKER_HUB_PASSWORD | docker login -u $DOCKER_HUB_USERNAME --password-stdin"
                        sh "docker-compose push"
                    }
                }
            }
        }
        /*
        stage('Nexus Deploy') {
            steps {
                withCredentials([usernamePassword(credentialsId: NEXUS_CREDENTIALS_ID, passwordVariable: 'NEXUS_PASSWORD', usernameVariable: 'NEXUS_USERNAME')]) {
                    sh """
                        mvn help:effective-settings -DshowPasswords=true -X
                        mvn deploy -DrepositoryId=nexus-snap -DaltDeploymentRepository=nexus::default::${NEXUS_URL} -Dnexus.username=${NEXUS_USERNAME} -Dnexus.password=${NEXUS_PASSWORD} -X
                    """
                }
            }
        } */
        stage('Pull from Docker Hub') {
            steps {
                script {
                    sh "docker pull ${DOCKER_HUB_REPO}:${BUILD_NUMBER}"
                }
            }
        }
        stage('Start Containers') {
            steps {
                script {
                    // Start both containers in detached modfe
                    sh 'docker-compose up -d'
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
