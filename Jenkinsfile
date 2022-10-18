#!/usr/bin/env groovy

node('iasset-jenkins-slave') {

    // -----------------------------------------------
    // --------------- Staging Branch ----------------
    // -----------------------------------------------
    if (env.BRANCH_NAME == 'staging') {

        stage('Clone and Update') {
            // clone via ssh-key!
            git(url: 'git@github.com:i-Asset/asset-repository.git', branch: env.BRANCH_NAME)
        }

        stage('Build Java') {
            sh 'mvn clean install -DskipTests'
        }

        stage('Run Tests') {
            sh 'mvn test'
        }

        stage('Build Service Container') {
            sh 'docker build . -f asset-repository-service/src/main/docker/Dockerfile \
                --build-arg JAR_FILE=asset-repository-service/target/*.jar \
                -t iassetplatform/asset-repository:staging'
        }

        stage('Push Docker') {
            sh 'docker push iassetplatform/asset-repository:staging'
        }

        stage('Deploy on staging server') {
            sh 'ssh staging "cd /srv/docker_setup/staging/ && ./run-staging.sh restart-single asset-repository"'
        }
    }

    // -----------------------------------------------
    // ---------------- Main Branch ------------------
    // -----------------------------------------------
    if (env.BRANCH_NAME == 'main') {

        stage('Clone and Update') {
            git(url: 'git@github.com:i-Asset/asset-repository.git', branch: env.BRANCH_NAME)
        }

        stage('Build Java') {
            sh 'mvn clean install -DskipTests'
        }

        stage('Run Tests') {
            sh 'mvn test'
        }
    }

    // -----------------------------------------------
    // ---------------- Release Tags -----------------
    // -----------------------------------------------
    if( env.TAG_NAME ==~ /^v\d+.\d+.\d+.*$/) {

        stage('Clone and Update') {
            git(url: 'git@github.com:i-Asset/asset-repository.git', branch: env.BRANCH_NAME)
        }

        stage('Set version') {
            sh 'mvn versions:set -DnewVersion=' + env.TAG_NAME
        }

        stage('Run Tests') {
            sh 'mvn clean test'
        }

        stage('Build Java') {
            sh 'mvn clean install -DskipTests'
        }

        stage('Build Service Container') {
            sh 'docker build . -f asset-repository-service/src/main/docker/Dockerfile \
                --build-arg JAR_FILE=asset-repository-service/target/*.jar \
                -t iassetplatform/asset-repository:' + env.TAG_NAME
        }

        stage('Push Docker Container') {
            sh 'docker push iassetplatform/asset-repository:' + env.TAG_NAME
            sh 'docker push iassetplatform/asset-repository:latest'
        }

        stage('Deploy on PROD server') {
            sh 'ssh prod "cd /data/deployment_setup/prod/ && sudo ./run-prod.sh restart-single asset-repository"'
        }

    }
}
