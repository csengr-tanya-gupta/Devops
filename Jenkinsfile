node {

    stage('Clone') {
        git branch: 'main',
            url: 'https://github.com/csengr-tanya-gupta/Devops.git'
    }

    stage('Build') {
        sh 'mvn clean package'
    }

    stage('Run') {
        sh '''
            export DISPLAY=:0
            java -jar target/*.jar
        '''
    }
}
