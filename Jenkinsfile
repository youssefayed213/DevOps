pipeline {
    agent any

    environment {
        DOCKER_HUB_REPO = 'ayedyoussef777/foyer-app' // Your Docker Hub repository
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials' // Jenkins credentials ID for Docker Hub
        SONAR_SERVER = 'SonarQube' // Name of the SonarQube server configured in Jenkins
        SONAR_TOKEN = credentials('jenkins-sonar') // Jenkins credentials ID for SonarQube token
        NEXUS_URL = 'http://localhost:8081/repository/maven-snapshots/'
        NEXUS_CREDENTIALS_ID = 'jenkins-nexus'
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
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=foyer22 -Dsonar.login=$SONAR_TOKEN -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml -X'
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
                sh 'mvn package'
            }
        }

        stage('Docker Build') {
                   steps {
                       // Build Docker image using Docker Compose (considering db and app services)
                       script {
                           sh 'docker-compose build --no-cache'
                       }
                   }
               }

               stage('Docker Push') {
                   steps {
                       // Push Docker image to Docker Hub
                       script {
                           withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, passwordVariable: 'DOCKER_HUB_PASSWORD', usernameVariable: 'DOCKER_HUB_USERNAME')]) {
                               sh "echo $DOCKER_HUB_PASSWORD | docker login -u $DOCKER_HUB_USERNAME --password-stdin"
                               sh "docker-compose push"
                           }
                       }
                   }
               }

       stage('Nexus Deploy') {
            steps {
                withCredentials([usernamePassword(credentialsId: NEXUS_CREDENTIALS_ID, passwordVariable: 'NEXUS_PASSWORD', usernameVariable: 'NEXUS_USERNAME')]) {
                   sh """
                      mvn help:effective-settings -DshowPasswords=true -X
                      mvn deploy -DrepositoryId=nexus-snap -DaltDeploymentRepository=nexus::default::${NEXUS_URL} -Dnexus.username=${NEXUS_USERNAME} -Dnexus.password=${NEXUS_PASSWORD} -X
                      """
                }
            }
        }


       /* stage('Pull from Docker Hub') {
            steps {
                // Pull the Docker image from Docker Hub for deployment
                script {
                    sh "docker pull ${DOCKER_HUB_REPO}:${BUILD_NUMBER}"
                }
            }
        }
    }*/

    post {
        success {
            echo 'Le pipeline de livraison continue a réussi !'
        }
        failure {
            echo 'Le pipeline de livraison continue a échoué.'
        }
    }
}
///dfgghfl
