pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Récupérer le projet depuis GitHub
                git branch: 'master', url: 'https://github.com/youssefayed213/DevOps.git'
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
    }
    
    post {
        success {
            echo 'Le build a réussi !'
        }
        failure {
            echo 'Le build a échoué.'
        }
    }
}
