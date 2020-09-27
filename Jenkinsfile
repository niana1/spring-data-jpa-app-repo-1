pipeline {

  agent any

  stages {	


	

	stage('Unit Test') {

	   steps {

			echo 'Project Testing stage'

			bat label: 'Test running', script: '''mvn test'''

	       

       		}

   	}

	  
	stage('Jacoco Coverage Report') {

        steps{

            jacoco()

		}

	}

	  

	stage('SonarQube'){

         steps{

            bat label: '', script: '''mvn sonar:sonar \

		 -Dsonar.host.url=http://localhost:9000 \

 		-Dsonar.login=e2036db293247cc969d108f1b42e6378c9dd9ec5'''

          }

      }

	  

	stage('Maven Package'){

		steps{

			echo 'Project packaging stage'

			bat label: 'Project packaging', script: '''mvn package'''

		}

	} 		

    

  }

}