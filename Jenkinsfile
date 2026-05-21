node {

    stage('Clone') {
        git branch: 'main',
            url: 'https://github.com/csengr-tanya-gupta/Devops.git'
    }

    stage('Build') {
        sh 'mvn clean package'
    }

    stage('Check JAR') {
        sh '''
            ls target
        '''
    }

    stage('Archive') {
        archiveArtifacts artifacts: 'target/*.jar'
    }
}
