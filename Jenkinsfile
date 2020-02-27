pipeline {
    agent any
    node {
     withGradle {
       sh './gradlew build'
      }
    }
    stages {
        stage('Build') {
            steps {
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }
    }
}
