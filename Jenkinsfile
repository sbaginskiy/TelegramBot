pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                archiveArtifacts artifacts: '*/*/*.jar', fingerprint: true
            }
        }
    }
}
