pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }
    }
}
