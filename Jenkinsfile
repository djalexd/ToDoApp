node {
   def mvnHome
   def dockerHome
   def dockerName
   def dockerRemote
   def commit_id
   stage('Preparation') {
      git 'https://github.com/djalexd/ToDoApp.git'
      mvnHome = tool 'Maven3'
      dockerName = 'todo-app'
      dockerRemote = 'tcp://192.168.50.10:2375'
      sh "git rev-parse --short HEAD > .git/commit-id"                        
      commit_id = readFile('.git/commit-id')
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
   stage('Build Docker') { 
      withDockerServer([uri: dockerRemote]) {
        def image = docker.build "${dockerName}"
        sh "docker tag ${dockerName} ${dockerName}:${commit_id}"
        // Image tagging does not work with docker remote API 1.12 (--force parameter was completely removed). 
        // image.tag(tagName = "${commit_id}", force = false)
      }
   }
   stage('Deploy to dev') {
      withDockerServer([uri: dockerRemote]) {
        sh "./src/main/scripts/stop-start-runtime-v1.sh ${dockerName} ${commit_id}"
      }
   }
}