pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './gradlew -D https.proxyHost=proxy1-rech -D https.proxyPort=3128 compileJava'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew -D https.proxyHost=proxy1-rech -D https.proxyPort=3128 test'
            }
        }
        stage('Sonar') {
            steps {
                sh "./gradlew -D https.proxyHost=proxy1-rech -D https.proxyPort=3128 sonar -Dsonar.projectKey=TpJenkins -Dsonar.projectName='TpJenkins' -Dsonar.host.url=http://172.17.0.1:9000 -Dsonar.token=sqp_1bd9a625c499a4931a695365972027c23c161cd1"
            }
        }
        stage('Jar') {
            steps {
                sh "./gradlew -D https.proxyHost=proxy1-rech -D https.proxyPort=3128 jar"
            }
        }
    }
}
