node {
   def mvnHome
   def dockerHome
   def dockerName
   def dockerRemote
   stage('Preparation') {
      git 'https://github.com/djalexd/ToDoApp.git'
      mvnHome = tool 'Maven3'
      dockerHome = tool 'docker'
      dockerName = 'todo-app'
      dockerRemote = 'DOCKER_HOST=tcp://192.168.50.10:2375'
   }
   stage('Build') {
      sh "'${mvnHome}/bin/mvn' -B -Dmaven.test.failure.ignore clean package"
      junit '**/target/surefire-reports/TEST-*.xml'
      archive 'target/*.jar'
   }
   stage('Code coverage') {
      sh "'${mvnHome}/bin/mvn' -B -Dmaven.test.failure.ignore -Pcoverage clean package"
      step([$class: 'JacocoPublisher', changeBuildStatus: true])
   }
   stage('Checkstyle') {
      sh "'${mvnHome}/bin/mvn' -B -Dmaven.test.failure.ignore -Pcheckstyle clean package"
      step([$class: 'CheckStylePublisher', canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '**/checkstyle-result.xml', unHealthy: ''])
   }
   stage('Build Docker image') {
      sh "${dockerRemote} '${dockerHome}/bin/docker' build -t ${dockerName} ."
   }
   stage('Deploy to dev') {
      sh "${dockerRemote} '${dockerHome}/bin/docker' run -d --name ${dockerName} ${dockerName}"
   }
   stage('Smoke test dev') {
      sh "echo 'Hello, world'"
   }
}