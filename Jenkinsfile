pipeline {

   // Setup Jenkins master and slave nodes

   agent {
      label "jenkins-slave"
   }

   // Configure git, maven, docker, Kubernetes and other supporting plugins in Jenkins prior to the build

   tools {
      maven 'MAVEN'
      dockerTool 'DOCKER'
   }

   environment {
     // You must set the following environment variables in Jenkins system configuration
     // ORGANIZATION_NAME
     // YOUR_DOCKERHUB_USERNAME

     SERVICE_NAME = "springbootapp"
     REPOSITORY_TAG="${YOUR_DOCKERHUB_USERNAME}/${ORGANIZATION_NAME}-${SERVICE_NAME}:${BUILD_ID}"
   }

   // Set GitHub id named 'GitHub' using credentialsId
   //

   stages {
      stage('Preparation') {
         steps {
            cleanWs()
            git credentialsId: 'GitHub', url: "https://github.com/${ORGANIZATION_NAME}/${SERVICE_NAME}"
         }
      }
      stage('Build') {
         steps {
            sh '''mvn clean package -DskipTests'''
         }
      }

      stage('Build Docker Image') {
         steps {
           sh 'docker image build -t ${REPOSITORY_TAG} .'
         }
      }

      //Set Docker id and Pswd named 'dockerhub_id' using credentialsId

      stage('DockerHub Push') {
         steps {
           withDockerRegistry(credentialsId: 'dockerhub_id', url: '') {
               sh 'docker push ${REPOSITORY_TAG}'
            }
         }
      }

      // Setup helm dependencies in Jenkins box

      stage('Helm Template') {
         steps {
           sh 'helm template ${WORKSPACE}/helm-chart > ${WORKSPACE}/deploy.yaml'
         }
      }

      // Setup AWS EKS cluster credentials named 'K8s' in credentialsId

      stage ('K8S Deploy') {
       steps {
            kubernetesDeploy(
               configs: 'deploy.yaml',
               kubeconfigId: 'K8s',
               enableConfigSubstitution: true
            )       
                   
         }
      }
   }
}