<<<<Readme for reporting Code Coverage to Wavefront>>>>


All about the Service: 

1. It is responsible for analyzing different formats of Code Coverage Reports (At present Jacoco and Sonar Reports) and sending it to Wavefront in form of Time Series metrics every 30 min.

2. It is also responsible for maintaining the threshold/gating values for the services. Using these threshold values we can configure our JaCoCo plugin to behave accordingly.
 
3. JaCoCo Reports are fetched from the Jenkins jobs and these Jenkins jobs need to be onboarded to this service. For Sonar reports you have to onboard the Sonar Report URL. 

4. There are some Enums which need to be used for onboarding the Service Properly.  
	3.1 Enum of testType: UNIT, FUNCTIONAL, E2E, OVERALL
	3.2 Enum of type: SONAR , JACOCO
	

Note: All the configuration is stored as JSON file in artifactory. Based upon the user's request we keep updating this config file.

5. JaCoCo threshold values for all the services are also reported to wavefront. 
   
6. API details can be found at  http://<host>:<port>/swagger-ui.html
	
 		
7. Run the service:
	6.1 Make a jar of the Service: " mvn clean install -DskipTests "
	6.2 Place this Jar on the Server where you want the service to run and run the following command :
		 java -jar code-coverage-0.0.1-SNAPSHOT.jar --artifactory_user=<artifactory_user> --artifactory_password=<artifactory_password>
	Note: There are many other configuration that can be overriden but we have given some default values.
	a. reporter_prefix 		
	b. wavefront_proxy_url	
	c. wavefront_proxy_port 
	d. JFROG_ARTIFACTORY_API_BASE_URL 
	e. CODE_COVERAGE_SERVICE_DEFINITION_FILE_LOCATION 
	
	Note: More Details can be found in application.properties file. 
	 
8. Viewing the metrics on Wavefront: 
	7.1 In wavefront navigate to Metrics and search for "<reporter_prefix>.code-coverage." and you will find all the corresponding metrics there being reported.
	7.2 In wavefront navigate to Metrics and search for "<reporter_prefix>.code-coverage.threshold." and you will find all the threshold values configured for all the services.
	Note: In cases of jobs being wrongly configured or some reports not coming correct , the service reports a 0 value against the metric name.   

	  