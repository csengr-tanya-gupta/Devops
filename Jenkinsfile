pipeline {

    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven'
    }

    stages {

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Check JAR') {
            steps {
                sh '''
                cd target
                ls
                '''
            }
        }

        stage('Run Game') {
            steps {
                sh '''
                java -jar target/*.jar
                '''
            }
        }

    }

    post {
        success {
            archiveArtifacts artifacts: 'target/*.jar'
        }
    }
}
