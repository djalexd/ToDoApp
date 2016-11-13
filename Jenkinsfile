node {
   def mvnHome
   stage('Preparation') {
      git 'https://github.com/djalexd/ToDoApp.git'
      mvnHome = tool 'Maven3'
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
}