// Jenkins Pipeline file
pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                git branch: 'main', 
                    url: 'https://github.com/RevWorkForce-PES/P2-RevWorkForce.git'
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building...'
                bat 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running tests...'
                bat 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Code Coverage - JaCoCo') {
            steps {
                echo 'Generating JaCoCo coverage report...'
                bat 'mvn jacoco:report'
            }
            post {
                always {
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java'
                    )
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Packaging...'
                bat 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', 
                                     fingerprint: true
                }
            }
        }
    }
    
    post {
        success { echo 'Pipeline SUCCESS!' }
        failure { echo 'Pipeline FAILED!' }
        always { cleanWs() }
    }
}