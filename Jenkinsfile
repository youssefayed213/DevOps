pipeline {
    agent any

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
