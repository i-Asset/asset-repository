#!/usr/bin/env groovy

node('iasset-jenkins-slave') {

    // -----------------------------------------------
    // --------------- Staging Branch ----------------
    // -----------------------------------------------
    if (env.BRANCH_NAME == 'staging') {

        stage('Clone and Update') {
            git(url: 'https://github.com/i-Asset/asset-repository.git', branch: env.BRANCH_NAME)
        }

        stage('Build Java') {
            sh 'mvn clean install -DskipTests'
        }

        stage('Run Tests') {
            sh 'mvn test'
        }

        stage('Build Docker') {
            sh 'docker build -t  -Ddocker.image.tag=staging'
        }

        stage('Push Docker') {
            sh 'docker push iassetplatform/asset-repository:staging'
        }

        stage('Deploy') {
            sh 'ssh staging "cd /srv/docker_setup/staging/ && ./run-staging.sh restart-single asset-repository"'
        }
    }

    // -----------------------------------------------
    // ---------------- Main Branch ------------------
    // -----------------------------------------------
    if (env.BRANCH_NAME == 'main') {

        stage('Clone and Update') {
            git(url: 'https://github.com/i-Asset/asset-repository.git', branch: env.BRANCH_NAME)
        }

        stage('Build Java') {
            sh 'mvn clean install -DskipTests'
        }

        stage('Build Docker') {
            sh 'mvn docker:build -Ddocker.image.tag=latest'
        }
    }

    // -----------------------------------------------
    // ---------------- Release Tags -----------------
    // -----------------------------------------------
    if( env.TAG_NAME ==~ /^v\d+.\d+.\d+.*$/) {

        stage('Clone and Update') {
            git(url: 'https://github.com/i-Asset/asset-repository.git', branch: env.BRANCH_NAME)
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

        stage('Build Docker') {
            sh 'mvn docker:build'
        }

        stage('Push Docker') {
            sh 'docker push iassetplatform/asset-repository:' + env.TAG_NAME
            sh 'docker push iassetplatform/asset-repository:latest'
        }

        stage('Deploy PROD') {
            sh 'ssh prod "cd /data/deployment_setup/prod/ && sudo ./run-prod.sh restart-single asset-repository"'
        }

    }
}
